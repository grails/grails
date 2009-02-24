import org.codehaus.groovy.grails.plugins.grailsui.GrailsUIException

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
class MenuTagLib {

    static namespace = 'gui'

    def grailsUITagLibService
    def menuTagLibService

    MenuItemList items
    def group

    // these two fields are static and thread-local because they need to be unique per session, but persist through
    // the tag recursion
    // submenuGroups contains a map
    static submenuGroup = [initialValue: { return [] }] as ThreadLocal<List<SubMenu>>;
    static void addNewSubmenuGroup(map) {
        submenuGroup.get() << map
    }
    static def getCurrentSubmenuGroup() {
        def currList = submenuGroup.get()
        if (currList.size()) return currList[-1]
        null
    }
    static def clearCurrentSubmenuGroup() {
        def list = submenuGroup.get()
        if (list.size() > 0) {
            list.remove(list[-1])
        }
    }

    /**
     * Creates a menubar for use at the top of your app or something
     */
    def menubar = { attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs,
                []
        )

        // fail-fast if someone is trying to use a menubar within a menu
        if (items != null) {
            throw new GrailsUIException("Cannot create a menubar within a menu.")
        }

        def id = attrs.remove('id')
        def renderTo = attrs.renderTo ? "'${attrs.remove('renderTo')}'" : 'document.body'
        items = new MenuItemList(root:true)

        body() // execute the body to process all inner workings of the menubar

        // we want to group all singles nodes in the top level list into list so they'll be properly wrapped in <ul>s
        items = menuTagLibService.groupRootNodes(items)
        out << """
<div id="${id}_div" class="yuimenubar">
    <div class="bd">
        ${items.menubarMarkup()}
    </div>
</div>
<script>
    YAHOO.util.Event.onDOMReady(function() {
        GRAILSUI.${id} = new YAHOO.widget.MenuBar("${id}_div", {${grailsUITagLibService.mapToConfig(attrs)}});
        GRAILSUI.${id}.render(${renderTo});
    });
</script>
"""
        items = null
    }

    /**
     * This can be used to create a context menu, or right click menu for something
     */
    def menu = { attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        hidedelay: 750,
                        lazyload: true,
                        autosubmenudisplay: true
                ],
                attrs,
                []
        )
        // fail-fast if someone is trying to put a menu within a menu
        if (items != null) {
            throw new GrailsUIException("Cannot create a menu within a menu or menubar.  Try using a submenu.")
        }

        def id = attrs.remove('id')
        def show = attrs.remove('show')
        items = new MenuItemList(root:true)

        body() // execute the body to process all inner workings of the menu

        // we want to group all singles nodes in the top level list into list so they'll be properly wrapped in <ul>s
        items = menuTagLibService.groupRootNodes(items)
        out << """
<div id="${id}_div" class="yuimenu">
    <div class="bd">
        ${items.menuMarkup()}
    </div>
</div>
<script>
    YAHOO.util.Event.onDOMReady(function() {
        GRAILSUI.${id} = new YAHOO.widget.Menu("${id}_div", {${grailsUITagLibService.mapToConfig(attrs)}});
        GRAILSUI.${id}.render();
        ${if (show) {return "GRAILSUI.${id}.show()"}}
    });
</script>
"""
        items = null
    }

    /**
     * Used within a menu or menubar to nest menuitems
     */
    def submenu = { attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId()
                ],
                attrs,
                []
        )
        def currentSubMenu = getCurrentSubmenuGroup()
        def id = attrs.remove('id')
        def subMenu = new SubMenu(id: id, label: attrs.label)
        addNewSubmenuGroup(subMenu)
        body()
        if (currentSubMenu) {
            subMenu.parent = currentSubMenu
            currentSubMenu.items << subMenu
        } else {
            items << subMenu
        }
        clearCurrentSubmenuGroup()
    }

    /**
     * Used within a menu to group things
     */
    def menugroup = {attrs, body ->

        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId()
                ],
                attrs,
                []
        )

        def id = attrs.remove('id')
        group = new MenuGroup(id:id, title: attrs.title)
        body()
        items << group
        group = null
    }

    /**
     * Basic unit of a menu :).
     */
    def menuitem = { attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId()
                ],
                attrs,
                ['url']
        )

        def id = attrs.remove('id')
        def url = createLink(attrs)

        def menuItem = new MenuItem(id:id, url:url, helpText:attrs.helpText, text:body())
        def subMenuGroup = getCurrentSubmenuGroup()
        if (group) {
            group.items << menuItem
        } else if (subMenuGroup) {
            subMenuGroup.items << menuItem
        } else {
            items << menuItem
        }
    }
}

/* Below are the units used internally for markup building */

class MenuItemList extends ArrayList {
    def root

    public MenuItemList() {}

    public MenuItemList(List input) {
        input.each { this << it }
    }

    def menuMarkup(count = 0) {
        doMarkup(count, 'menu')
    }

    def menubarMarkup(count = 0) {
        doMarkup(count, 'menubar')
    }

    private def doMarkup(count, type) {
        def markup = ''
        def firstClass = count == 0 ? 'class="first-of-type"' : ''
        if (!root) markup += "<ul ${firstClass}>"
        def itemCount = 0
        markup += this.collect {
            it."${type}Markup"(itemCount++)
        }.join('\n')
        if (!root) markup += "</ul>"
        markup
    }
}

class MenuItem {
    def id
    def url
    def text
    def helpText

    def menuMarkup(count) { markup(count) }
    def menubarMarkup(count) { markup(count) }

    def markup(count) {
        def myHelpText = helpText ? "<em class=\"helptext\">${helpText}</em> " : ''
        def firstClass = count == 0 ? 'first-of-type' : ''
        """\
<li id="${id}" class="yuimenuitem ${firstClass}">
    <a class="yuimenuitemlabel" href="${url}">${text}${myHelpText}</a>
</li>"""
    }

    String toString() {
        "MenuItem $text"
    }
}

class SubMenu {
    def parent
    def id
    def label
    def type
    MenuItemList items = new MenuItemList()

    def menuMarkup(count) {
        type = 'menu'
        markup(count)
    }
    def menubarMarkup(count) {
        type = 'menubar'
        markup(count)
    }

    def markup(count) {
        def itemCnt = 0
        def myItemMarkup = items.collect {
            it.menuMarkup(itemCnt++)
        }.join('\n')

        def myClassType = parent || type == 'menu' ? 'menu' : 'menubar'
        def firstClass = count == 0 ? 'first-of-type' : ''
        """
            <li id="${id}" class="yui${myClassType}item ${firstClass}">

                <a class="yui${myClassType}itemlabel" href="#sub_${id}">
                    ${label}
                </a>

                <!-- A submenu -->

                <div id="sub_${id}" class="yuimenu">
                    <div class="bd">
                        <ul class="first-of-type">
                            ${myItemMarkup}
                        </ul>
                    </div>
                </div>

            </li>
        """
    }

    String toString() {
        "Submenu $label"
    }
}

class MenuGroup {
    def id
    def title
    MenuItemList items = new MenuItemList()

    def menuMarkup(count) { markup(count) }
    def menubarMarkup(count) { markup(count) }

    def markup(count) {
        def itemCnt = 0
        def myItemMarkup = items.collect {
            it.menuMarkup(itemCnt++)
        }.join('\n')

        def firstClass = count == 0 ? 'class="first-of-type"' : ''
        def titleMarkup = title ?"<h6 ${firstClass}>${title}</h6>\n" : ''
        """ ${titleMarkup}
        <ul>
            ${myItemMarkup}
        </ul>"""
    }
}
