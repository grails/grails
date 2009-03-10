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

changeSet(id:'IntegrateTaggablePlugin', author:'Rhyolight') {
    dropTable(tableName:'plugin_tags')
    dropTable(tableName:'tag')
}

changeSet(id:'IntegrateCommentablePlugin', author:'Rhyolight') {
    dropTable(tableName:'content_comment')
    dropTable(tableName:'plugin_comment')
    dropColumn(tableName:'comment', columnName:'user_id')
}

changeSet(id:'UpdateTaggableTagLink', author:'Rhyolight') {
    dropColumn(tableName:'tag_links', columnName:'tag_class')
}

changeSet(id:'IntegrateRateablePlugin', author:'Rhyolight') {
    dropTable(tableName:'rating')
}