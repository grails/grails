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
class TabTagLib {

    static namespace = "gui"

    def grailsUITagLibService

    // these two fields are static and thread-local because they need to be unique per session, but persist through
    // the tag recursion
    // tabs contains a map of tabView ID ==> tab configs
    static tabs = [initialValue: { return [:] }] as ThreadLocal<Map<String, List>>;
    // tabViewIds is a list of tab view ids used to step forward and back through nested tabs
    static tabViewIds = [initialValue: { return [] }] as ThreadLocal<List>;

    static void setCurrentTabViewId(def id) {
        tabViewIds.get() << id
    }

    static def getCurrentTabViewId() {
        tabViewIds.get()[-1]
    }

    static void clearCurrentTabViewId() {
        def list = tabViewIds.get()
        if (list.size() > 0) {
            list.remove(list[-1])
        }
    }

    def tabView = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: "tabView_${grailsUITagLibService.getUniqueId()}"
                ],
                attrs,
                []
        )

        // this is the list of tabs for this one tab view
        def tabViewTabs = []
        // set this tabview id in the threadlocal list of ids
        setCurrentTabViewId(attrs.id)
        // set our local list of tabs into the tabs map for this tabview id
        tabs.get()[attrs.id] = tabViewTabs

        // have to execute the body to ensure all tab tags contained within are executed
        body()
        // the tabViewTabs should now be loaded with tabs data from any tabs that were executed in the body() call

        def id = attrs.id
        def jsid = grailsUITagLibService.makeJavascriptFriendly(id)
        def tabNav = tabViewTabs.findAll { it.nav }.collect { it.nav }.join('\n')
        def tabContent = tabViewTabs.findAll { it.content }.collect { it.content }.join('\n')
        out << """
        <div id="${id}" class="yui-navset">
            <div class="tabOuter">
                <div class="tabInner">
                    <ul class="yui-nav">
                        ${tabNav}
                    </ul>
                </div><!-- end #tabInner -->
            </div><!-- end #tabOuter -->
            <div class="yui-content">
                ${tabContent}
            </div>
        </div>
        <script type="text/javascript">
            GRAILSUI.${id} = new YAHOO.widget.TabView('${id}');
        """
        tabViewTabs.eachWithIndex { tab, i ->
            if (tab.config) {
                def tabId = grailsUITagLibService.makeJavascriptFriendly(tab.config.remove('id'))
                out << "GRAILSUI.${tabId} = new YAHOO.widget.Tab({${grailsUITagLibService.mapToConfig tab.config}});"
                out << "GRAILSUI.${jsid}.addTab(GRAILSUI.${tabId} , $i );\n"
            }
        }
        if (attrs.activeTab) {
            out << "GRAILSUI.${jsid}.activeTab = GRAILSUI.${grailsUITagLibService.toJS(attrs.activeTab)};" 
        }
        out << '</script>'
        clearCurrentTabViewId()
    }

    def tab = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        active: false,
                        cached: false
                ],
                attrs,
                ['label']
        )

        if (attrs.controller && attrs.action) {
            def id = attrs.remove('id')
            attrs.dataSrc = createLink(attrs)   // set dynamic dataSrc if necessary
            attrs.id = id
        }
        def classes = "class='${attrs.active ? 'selected' : ''} ${attrs['class'] ? attrs['class'] : ''}'"
        if ((attrs.controller && attrs.action) || attrs.dataSrc) {
            // dynamically rendered config
            tabs.get()[getCurrentTabViewId()] << [config : attrs]
        } else {
            def nav = """<li ${classes}><a href="#${attrs.id}"><span class="tabLeft"></span><em>${attrs.label}</em><span class="tabRight"></span></a></li>\n"""
            def content = """<div id="${attrs.id}">${body()}</div>\n"""
            tabs.get()[getCurrentTabViewId()] << [nav:nav, content:content]
        }
    }

}