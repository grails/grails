/*
 * author: Matthew Taylor
 */
class MenuTagLibService {

    static transactional = false

    def groupRootNodes = { list ->
        def result = new MenuItemList(root:true)
        def rootCache = new MenuItemList()
        list.each { node ->
            if (node instanceof MenuItem || node instanceof SubMenu) {
                rootCache << node
            } else {
                if (rootCache.size()) {
                    result << rootCache
                    rootCache = new MenuItemList()
                }
                result << node
            }
        }
        if (rootCache.size()) {
            result << rootCache
        }
        result
    }

}