/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class DialogTagLib {

    static namespace = "gui"
    def grailsUITagLibService
    def triggerWraps = [
        link:["<a href=\"javascript:void(0);\">","</a>"],
        button:["<button>","</button>"]
    ]

    /**
     * Possible attrs (aside from YAHOO.widgets.Dialog config):
     *  id:         Used to create DOM elements and the div that is transformed into the Dialog.  There are some
     *              additional elements that use this to find their names as well, but they are internal.
     *  title:      Text that goes into the header bar of the Dialog
     *  triggers:   List of Maps that contain data used to attach listeners to the Dialog.  Each list element should be
     *              a Map with the key=action and the value=configuration.
     *                  Ex:     triggers="[
     *                              show:[id:'showTriggerId', on:'click'],
     *                              hide:[id:'hideTriggerId', on:'click']
     *                          ]"
     *              This would try to attach event listeners coming from click events on any DOM element with the
     *              specified ids.  On click, the event would trigger the event specified as the key (show or hide, in
     *              this case).
     *
     *              Another example where you can ask the tag to create a trigger source for you:
     *                  EX:     triggers="[show:[type:'link', text:'Show Dialog!', on:'click']]"
     *              With this trigger configuration, the tag would create a link for you with the specified text that
     *              would trigger the Dialog show action upon click.
     *  buttons:    If you need special event handling, this allows you so specify your buttons and handlers.  By
     *              default, there are two possible configurations if you do not specify your own button config:
     *                  1. 'OK' button that closes the Dialog (when form="true" is not specified [see below])
     *                  2. 'Submit' and 'Cancel', which either submit form data form the Dialog remotely to the server,
     *                     or close the Dialog without doing anything (when form="true)
     *  form:       Specifies that you want to process form data from the Dialog.  This will configure default 'Submit
     *              and 'Cancel' buttons for you, and you cannot change them at this point.  Be sure to specify the
     *              'controller', 'action', and 'params' (optional) if you use this.
     *  controller: Used to construct the URL to submit the form.
     *  action:     Used to construct the URL to submit the form.
     *  params:     (optional) Used to construct the URL to submit the form.
     *  update:     (optional) Used to render the server response text back to after submit.
     */
    def dialog = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        title: 'Simple Dialog',
                        form: false,
                        formUpload: false,
                        method: 'POST',
                        clazz: "yui-skin-sam",
                        fixedcenter: true,
                        visible: false,
                        params: [],
                        constraintoviewport: true,
                ],
                attrs
        )

        def clazz = attrs.remove('clazz')
        def title = attrs.remove('title')
        def method = attrs.remove('method')
        def form = attrs.remove('form')
        def formUpload = attrs.remove('formUpload') // if there is a file upload
        def update = attrs.remove('update')
        def triggers = attrs.remove('triggers')
        // must remove the id before calling createLink, because it will assume it is a domain id, not an element id
        def id = attrs.remove('id')
        def jsid = grailsUITagLibService.toJS(id)
        def content = body()
        def richEditors = [] // list of RTEs within the dialog
        // if there should be a form involved, surround our body with a form element and proper action URL
        if (form) {
            attrs.dataURL = createLink(attrs)
            
            // look through content for richEditors
            if (content.contains('SimpleEditor')) {
                content.split('SimpleEditor')[0..-2].each {
                    def start = it.lastIndexOf('GRAILSUI.')
                    def stop = it.lastIndexOf('=')-1
                    richEditors << it[start..stop].trim() // will process these later to write out code that saves HTML before submit
                }
            }

            def uploadAttrib = formUpload ? "enctype='multipart/form-data'" : ''
                        
            content = """
            <form id="${id}Form" ${uploadAttrib} method='${method}' action='${attrs.dataURL}'>
                ${content}
            </form>
            """
            // default buttons for form submission
            if (!attrs.buttons) {
                attrs.buttons = [
                    [text:'Submit', handler: 'function() {this.submit();}', isDefault: true],
                    [text:'Cancel', handler: 'function() {this.cancel();}', isDefault: false]
                ]
            }
        } else {
            // if there is not a form involved, set up a default OK button
            if (!attrs.buttons) {
                attrs.buttons = [[text:'OK', handler: 'function() {this.cancel();}', isDefault: true]]
            }
        }
        
        // element the dialog will use to render its content, surrounded by a div to style it
        out << """
        <div class="${clazz}">
            <div id="${id}">
                <div class="hd">${title}</div>
                <div class="bd">
                    ${content}
                </div>
            </div>
        </div>
        """

        // check if any triggers need some source element rendered
        triggers.findAll{key, val ->
            !val.containsKey('id')
        }.each { key, val ->
            def wrap = triggerWraps."${val.type}"
            out << """
                <span id="${id}_showTrigger">${wrap[0]}${val.text}${wrap[1]}</span>
            """
            // now that it has a source element, alter this trigger to fit in with the rest of them
            val.id = "${jsid}_showTrigger"
        }

        out << """<script>
            function init_dlg_${jsid}() {
                // Instantiate the Dialog
                GRAILSUI.${jsid} = new YAHOO.widget.Dialog("${id}",
                { ${grailsUITagLibService.mapToConfig attrs} });
                GRAILSUI.${jsid}.render(document.body);
                ${addListeners(triggers, jsid)}
        """
        // after the dialog is defined, we need to add the submission callbacks if there is a form involved
        if (form) {
            // only need to process the success response if there is an 'update' attribute to replace
            def successHandler = ''
            if (update) {
                successHandler = """
                var replaceDiv = document.getElementById("${update}");
                GRAILSUI.util.replaceWithServerResponse(replaceDiv, o);
                """
            }
            out << """
                var handleSuccess = function(o) {
                    ${successHandler}
                };

                var handleFailure = function(o) {
                    alert("Submission failed: " + o.status);
                };
                GRAILSUI.${jsid}.callback = {
                    success: handleSuccess,
                    failure: handleFailure
                }
            """
            // also call saveHTML() on all rich text editors within dialog before submit
            if (richEditors.size()) {
                def editorSaveLogic = richEditors.collect {
                    "${it}.saveHTML();"
                }.join('\n')
                out << """
                    GRAILSUI.${jsid}.beforeSubmitEvent.subscribe(function() {
                        ${editorSaveLogic}
                    });
                """
            }
        }
        out << """
            }
            YAHOO.util.Event.onDOMReady(init_dlg_${jsid});
        </script>"""
    }

    private def addListeners(triggers, dialogId) {
        def listeners = ''
        triggers.each { key, val ->
            listeners += """
                YAHOO.util.Event.addListener("${val.id}", "${val.on}", GRAILSUI.${dialogId}.${key}, GRAILSUI.${dialogId}, true);
            """
        }
        listeners
    }
}
