changeSet(id:'PluginPortalComments', author:'Rhyolight') {
    dropTable(tableName:'blog_entry_comment')
    dropColumn(tableName:'comment', columnName:'email')
    dropColumn(tableName:'comment', columnName:'poster')
}

changeSet(id:'IncreaseCommentBodySize', author:'Rhyolight') {
    modifyColumn(tableName:'comment') {
        column(name:'body', type:'TEXT')
    }
}
