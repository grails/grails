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

class InputTagLib {

    static namespace = "gui"

    def grailsUITagLibService
    def datePickerService

    def richEditor = {attrs ->

        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        value: '',
                        height: '330px',
                        width: '522px',
                        dompath: true
                ],
                attrs,
                []
        )
        // remove id and value from the attrs before building config object for SimpleEditor constructor
        def id = attrs.remove('id')
        def jsid = grailsUITagLibService.toJS(id)
        def value = attrs.remove('value')

        // set up required config values
        attrs.handleSubmit = true

        // create a textarea with value for the SimpleEditor to transform
        out << """
            <div class="yui-skin-sam">
                <textarea id="${id}" name="${id}">${value}</textarea>
            </div>"""
        // build the SimpleEditor, using the textarea's id and the the attrs to build its config
        out << """
            <script>
            GRAILSUI.${jsid} = new YAHOO.widget.SimpleEditor(
                '${id}', {${grailsUITagLibService.mapToConfig attrs}}
            );
            GRAILSUI.${jsid}.render();
            </script>
        """
    }

    def autoComplete = {attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        resultName: 'result',
                        labelField: 'name',
                        idField: 'id',
                        'class': '',
                        style: '',
                        useShadow: false,
                        minQueryLength: 0,
                        queryDelay: 0.2,
                        value: '',
                        title: '',
                        connMethodPost: true,
                        dependsOn: [:],
                        queryMatchContains: false
                ],
                attrs,
                ['action|url|options']
        )

        def containerId = "c" + grailsUITagLibService.getUniqueId()
        def resultName = attrs.remove('resultName')
        def idField = attrs.remove('idField')
        def labelField = attrs.remove('labelField')
        def id = attrs.remove('id')
        def jsid = grailsUITagLibService.toJS(id)
        def name = attrs.name ? attrs.name : id
        def value = attrs.remove('value')
        def filter = attrs.remove('filter')
        def filterBy = attrs.remove('filterBy')
        def queryAppend = attrs.remove('queryAppend')
        def connMethodPost = attrs.remove('connMethodPost')
        def dependsOn = attrs.remove('dependsOn')
        def options = attrs.remove('options')

        // these apparently just need to be removed from the attrs before they are converted into a js config object
        def title = attrs.remove('title')
        def style = attrs.remove('style')
        def cssClass = attrs.remove('class')

        // dependsOn can be one String, or a map of values.  If it is one String, translate it to a map with default values
        if (dependsOn instanceof String) {
            dependsOn = [value: dependsOn, label: 'dependsOnValue', useId: false]
        } else if (dependsOn) {
            // must have a value
            if (!dependsOn.value) {
                throw new GrailsUIException("gui:autoComplete dependsOn tag cannot be used without a 'value' key in the attribute mapping.")
            }
            // set up defaults
            if (!dependsOn.label) dependsOn.label = 'dependsOnValue'
            if (!dependsOn.useId) dependsOn.useId = false
        }

        // the javascript logic to add if there is a dependency on another autoComplete's value
        def dependsOnListenerLogic = dependsOn ? """
            var dependencySelectHandler = function(sType, aArgs) {
                var oMyAcInstance = aArgs[0];
                var aData = aArgs[2];
                GRAILSUI.${jsid}.dataSource.scriptQueryAppend = "${ filter && filterBy ? "filter=${filter}&filterBy=${filterBy}&" : "" }${queryAppend ? "${queryAppend}&" : ''}${dependsOn.label}=" + aData[${dependsOn.useId ? '1' : '0'}];
            };
            GRAILSUI.${grailsUITagLibService.toJS(dependsOn.value)}.itemSelectEvent.subscribe(dependencySelectHandler);
        """ : ''

        out << """
        <div class="yui-skin-sam yui-ac">
            <input type="text" style="display:none" id="${id}_id" name="${name}_id" hidden="true"/>
            <input type="text" class="yui-ac-input" id="${id}" name="${name}" value="${value}" hidden="true"/>
            <div class="yui-ac-container" id="${containerId}">
            </div>
        </div>
        """
        out << """
                <script>
                    function init_${jsid}_ac() {
        """
        // of there are options, that means a LocalDataSource
        if (options) {
            out << """
                        var dataSource = new YAHOO.util.LocalDataSource([${grailsUITagLibService.listToConfig options}]);
                        dataSource.responseType = YAHOO.util.DataSource.TYPE_JSARRAY
            """
        } else {
            def dataURL = createLink(attrs)
            //id was removed for the call to createLink
            out << """
                        var dataSource = new YAHOO.widget.DS_XHR('${dataURL}', ['${resultName}','${labelField}','${idField}']);
                        dataSource.scriptQueryAppend = "${ filter && filterBy ? "filter=${filter}&filterBy=${filterBy}&" : "" }${queryAppend ? "${queryAppend}&" : ''}";
                        dataSource.connMethodPost=${connMethodPost};
            """
        }

        out << """
                    GRAILSUI.${jsid} = new YAHOO.widget.AutoComplete('${id}', '${containerId}', dataSource);
                    GRAILSUI.${jsid}.dataSource = dataSource;
                    GRAILSUI.${jsid}.prehighlightClassName = 'yui-ac-prehighlight';
                    GRAILSUI.${jsid}.minQueryLength = 0;
                    GRAILSUI.${jsid}.textboxFocusEvent.subscribe(function() {
                        var sInputValue = YAHOO.util.Dom.get('${id}').value;
                        if (sInputValue.length === 0) {
                            var oSelf = this;
                            setTimeout(function() {
                                oSelf.sendQuery(sInputValue);
                            }, 0);
                        }
                    });
                    // here, we are populating a hidden input with the selected id so it will be sent with the form
                    var itemSelectHandler = function(sType, aArgs) {
                        var oMyAcInstance = aArgs[0];
                        var elListItem = aArgs[1];
                        var aData = aArgs[2];
                        var id = aData[1];
                        document.getElementById('${id}_id').value=id;
                    };
                    GRAILSUI.${jsid}.itemSelectEvent.subscribe(itemSelectHandler);
"""
        // because the autoComplete doesn't access a config object like most YUI widgets, we'll need to
        // add each config value to the autoComplete afterwards
        attrs.each { key, val ->
            out << "GRAILSUI.${jsid}.${key} = ${grailsUITagLibService.valueToConfig(val)};\n"
        }
        out << """
                    ${dependsOnListenerLogic}
                }
            YAHOO.util.Event.onDOMReady(init_${jsid}_ac);
            </script>"""
    }

    def datePicker = {attrs ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        iframe: false,
                        hide_blank_weeks: true,
                ],
                attrs
        )
        def showButtonId = grailsUITagLibService.getUniqueId()
        def id = attrs.remove('id')
        boolean includeTime = attrs.remove('includeTime')
        def jsid = grailsUITagLibService.toJS(id)
        def label = attrs.title ? "<label for='date'>${attrs.remove('title')}</label>" : ''
        // if there is a date value, need to break it up, set the value input text, and
        // set the calendar properly
        def d = [hour: 12, minute: 0, second: 0, ampm: 'AM'] // used to store the selected date
        def value = ''
        def defaultDateFormat = 'dd/MM/yy'
        if (includeTime) defaultDateFormat = 'dd/MM/yy HH:mm:ss a'
        def formatString = attrs.remove('formatString') ?: defaultDateFormat

        def navigationToInitialDateLogic = ''
        if (attrs.value) {
            def dateValue = attrs.remove('value')
            def calendar
            if (dateValue instanceof Date) {
                calendar = new GregorianCalendar()
                calendar.time = dateValue
            } else if (dateValue instanceof Calendar) {
                calendar = dateValue
            } else {
                throw new GrailsUIException("The 'value' sent into datePicker must be a Date or Calendar object.")
            }
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(formatString);
            value =  sdf.format(calendar.time)
            d = datePickerService.calendarSplit(calendar)
            // now add 'selected' to the attrs to be passed into the Calendar and set the initial date selection
            attrs.selected = "${d.month}/${d.day}/${d.year}"
            navigationToInitialDateLogic += """
                GRAILSUI.${jsid}.setMonth(${d.month - 1})
                GRAILSUI.${jsid}.setYear(${d.year})
            """
        }

        def timeInput = ''
        def timeTackLogic = ''
        if (includeTime) {
            def buildOptions = {from, to, selected ->
                if (selected instanceof String) selected = selected.toInteger()
                def result = ''
                (from..to).each {i ->
                    def selText = selected == i ? "selected='selected' " : ''
                    result += "<option value=\"${i}\" ${selText}>${i}</option>"
                }
                result
            }
            timeInput = """
                <div class="timeInput">
                <select id="${id}_hr">${buildOptions 1, 12, d.hour}</select>:<select id="${id}_min">
                    ${buildOptions 0, 60, d.minute}
                </select>:<select id="${id}_sec">
                    ${buildOptions 0, 60, d.second}
                </select><select id="${id}_am_pm">
                    <option value="AM" ${if (d.ampm == 'AM') return "selected='selected'" else return ''}>AM</option>
                    <option value="PM" ${if (d.ampm == 'PM') return "selected='selected'" else return ''}>PM</option>
                </select>
                </div>
            """
            timeTackLogic = """
              //Retrievet the time values
                var hr = YAHOO.util.Dom.get("${id}_hr").value;
                var min = YAHOO.util.Dom.get("${id}_min").value;
                var sec = YAHOO.util.Dom.get("${id}_sec").value;
                var ampm = YAHOO.util.Dom.get("${id}_am_pm").value;
             //Handle special cases with some padding logic so that AM and PM state is preserved correctly.
             // this is due to javascript having a 24 hour clock.
               if(ampm=='PM' && hr==12){
                 hr=parseInt(hr);
                 }
               else if(ampm=='PM' && parseInt(hr)< parseInt(12)){
                hr=parseInt(hr)+parseInt(12)
               }
               else if(ampm=='AM' && parseInt(hr)==parseInt(12)  ){
                 hr=parseInt(0);
               }
               selDate.setHours(hr);
               selDate.setMinutes(min);
               selDate.setSeconds(sec);
                var formatString="${formatString}";
                var sdf=new GRAILSUI.SimpleDateFormat();
            """
        }

        // output the markup necessary to transform
        out << """
           <div class="datePicker">
               <div class="datefield">
                  ${label}<input type="text" id="${id}" name="${id}" value="${value}" />
                    <button type="button" id="${showButtonId}" name="${showButtonId}" title="Show Calendar">
                        <img src="${createLinkTo(dir: pluginContextPath + '/images', file: 'cal.gif')}" width="18" height="18" alt="Calendar" >
                    </button>
               </div>
               <div id="${id}_calContainer" class="calendarContainer">
                  <div class="hd"></div>
                  <div class="bd">
                     <div id="${id}_cal"></div>
                     ${timeInput}
                  </div>
               </div>
           </div>
        """
        // now the script to create the calendar
        out << """
        <script>
            YAHOO.util.Event.onDOMReady(function(){

                GRAILSUI.${jsid} = new YAHOO.widget.Calendar("${id}_cal", {
                    ${grailsUITagLibService.mapToConfig attrs}
                });
                
                ${navigationToInitialDateLogic}

                function ${jsid}SelectHandler() {
                    if (GRAILSUI.${jsid}.getSelectedDates().length > 0) {
                        var selDate = GRAILSUI.${jsid}.getSelectedDates()[0];
                          ${timeTackLogic}
                        var dateFormatString="${formatString}";
                        var simpleDateFormat=new GRAILSUI.SimpleDateFormat(selDate);
                        var newDateValue = simpleDateFormat.formatDate(dateFormatString);
                        YAHOO.util.Dom.get("${id}").value = newDateValue;
                    } else {
                        YAHOO.util.Dom.get("${id}").value = "";
                    }
                    GRAILSUI.${jsid}Panel.hide();
               }
               GRAILSUI.${jsid}.selectEvent.subscribe(${jsid}SelectHandler);

                var ${jsid}CloseHandler = function(e, oArgs, me) {
                    GRAILSUI.${jsid}Panel.hide();
                    return false;   // returning false stops the even propagation
                };
                GRAILSUI.${jsid}.beforeHideEvent.subscribe(${jsid}CloseHandler);
                GRAILSUI.${jsid}Panel = new YAHOO.widget.Panel("${id}_calContainer", {
                    context:["${showButtonId}", "tl", "bl"],
                    buttons:[ ],
                    width:"18em",  // Sam Skin panel needs to have a width defined (7*2em + 2*1em = 16em). [+2 for time input -- MAT]
                    draggable:false,
                    close:false
                });
                GRAILSUI.${jsid}.render();
                GRAILSUI.${jsid}Panel.render();

                // Using panel.hide() instead of visible:false is a workaround for an IE6/7 known issue with border-collapse:collapse.
                GRAILSUI.${jsid}Panel.hide();

                GRAILSUI.${jsid}.renderEvent.subscribe(function() {
                    // Tell panel it's contents have changed, Currently used by for IE6/Safari2 to sync underlay size
                    GRAILSUI.${jsid}Panel.fireEvent("changeContent");
                });

                YAHOO.util.Event.on("${showButtonId}", "click", function() {
                    var buttonRegion = YAHOO.util.Dom.getRegion('${showButtonId}');
                    var buttonHeight = buttonRegion.bottom - buttonRegion.top;
                    var buttonWidth = buttonRegion.right - buttonRegion.left;
                    var xy = YAHOO.util.Dom.getXY('${showButtonId}');
                    var newXY = [xy[0] + buttonWidth, xy[1] + buttonHeight];
                    YAHOO.util.Dom.setXY('${id}_calContainer_c', newXY);
                    GRAILSUI.${jsid}Panel.show();
                    if (YAHOO.env.ua.opera && document.documentElement) {
                        // Opera needs to force a repaint
                        document.documentElement.className += "";
                    }
                });
            });
        </script>
        """
    }
}
