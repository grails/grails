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
import org.codehaus.groovy.grails.plugins.grailsui.GrailsUIException

class DisplayTagLib {

    static namespace = "gui"

    def grailsUITagLibService

    def expandablePanel = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        expanded: false,
                        closable: false,
                        bounce: true
                ],
                attrs,
        )
        def bounceMarkup = attrs.bounce ? 'rel="bounceOut"' : ''
        def expanded = ''
        if (grailsUITagLibService.makeJavascriptFriendly(attrs.expanded)) expanded = ' selected'
        def title = 'Expandable Panel Dummy title'
        if (attrs.title) title = attrs.title
        def closeAction = attrs.closable ? '<a href="#" class="accordionRemoveItem">&nbsp;</a>' : ''
        out << """
            <div id="${attrs.id}" class="yui-skin-sam yui-cms-accordion multiple fade fixIE" ${bounceMarkup}>
                <div class="yui-cms-item yui-panel${expanded}">
                    <div class="hd ${attrs['class'] ? attrs['class'] : ''}">${title}</div>

                    <div class="bd">
                      <div class="fixed">
                        <p>
                          ${body()}
                        </p>
                      </div>
                    </div>

                    <div class="actions">
                      <a href="#" class="accordionToggleItem">&nbsp;</a>
                      ${closeAction}
                    </div>
                </div>
            </div>
        """
    }

    def accordion = {attrs, body  ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        multiple: false,
                        persistent: false,
                        fade: false,
                        bounce: false,
                        slow: false
                ],
                attrs,
        )
        def config = ['class="yui-cms-accordion fixIE ',
            (attrs.multiple ? 'multiple ' : ''),
            (attrs.persistent ? 'persistent ' : ''),
            (attrs.fade ? 'fade' : ''),
            (attrs.slow ? 'slow' : ''),
            '"',
            (attrs.bounce ? ' rel="bounceOut"' : '')].join('')
        out << """
    		<div id="${attrs.id}" ${config}>
    			${body()}
    		</div>
        """
    }

    def accordionElement = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        selected: false
                ],
                attrs,
                ['title']
        )
        out << """
            <div class="yui-cms-item yui-panel ${attrs.selected ? 'selected' : ''}">
                <div class="hd">${attrs.title}</div>
                <div class="bd">
                    <div id="${attrs.id}" class="fixed">
                        ${body()}
                    </div>
                </div>
                <div class="actions">
                    <a href="#" class="accordionToggleItem">&nbsp;</a>
                </div>
            </div>
        """
    }

    def toolTip = {attrs, body ->

        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs
        )

        def id = attrs.remove('id')
        def text = attrs.remove('text')

        // if  the tooltip text is set, we can assume that we want to use it, not a server call
        if (text) {
            // surround body with span that includes the tooltip trigger
            out << "<span id='$id' class='yui-tip' title='${text}'>${body()}</span>"
        } else {
            def dataUrl
            try {
                dataUrl = createLink(attrs)
            } catch (Exception e) {
                throw new GrailsUIException("There was not enough information in the gui:toolTip tag to create the link." +
                  "  Either a 'text' attribute, 'controller'/'action' atributes, or a 'url' attribute is required.")
            }
            // otherwise create the span with an URL to call to populate the toolTip
            out << "<span id='$id' url='$dataUrl' class='yui-tip'>${body()}</span>"
        }
    }
}
