changeSet(id:'PluginPortalComments', author:'Rhyolight') {
    preConditions(onFail:"MARK_RAN") {
        tableExists(schemaName:'grails', tableName:'blog_entry_comment')
        columnExists(schemaName:'grails', tableName:'comment', columnName:'email')
        columnExists(schemaName:'grails', tableName:'comment', columnName:'poster')
    }
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
    preConditions(onFail:"MARK_RAN") {
        tableExists(schemaName:'grails', tableName:'plugin_tags')
        tableExists(schemaName:'grails', tableName:'tag')
    }
    dropTable(tableName:'plugin_tags')
    dropTable(tableName:'tag')
}

changeSet(id:'IntegrateCommentablePlugin', author:'Rhyolight') {
    preConditions(onFail:"MARK_RAN") {
        tableExists(schemaName:'grails', tableName:'content_comment')
        tableExists(schemaName:'grails', tableName:'plugin_comment')
        columnExists(schemaName:'grails', tableName:'comment', columnName:'user_id')
    }
    dropTable(tableName:'content_comment')
    dropTable(tableName:'plugin_comment')
    dropColumn(tableName:'comment', columnName:'user_id')
}

changeSet(id:'UpdateTaggableTagLink', author:'Rhyolight') {
    preConditions(onFail:"MARK_RAN") {
        columnExists(schemaName:'grails', tableName:'tag_links', columnName:'tag_class')
    }
    dropColumn(tableName:'tag_links', columnName:'tag_class')
}

changeSet(id:'IntegrateRateablePlugin', author:'Rhyolight') {
    preConditions(onFail:"MARK_RAN") {
        tableExists(schemaName:'grails', tableName:'rating')
    }
    dropTable(tableName:'rating')
}