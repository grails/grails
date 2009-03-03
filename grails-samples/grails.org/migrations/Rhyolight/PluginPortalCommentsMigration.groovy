changeSet(id:'PluginPortalComments', author:'Rhyolight', failOnError:false) {
    dropTable(tableName:'blog_entry_comment')
    dropColumn(tableName:'comment', columnName:'email')
    dropColumn(tableName:'comment', columnName:'poster')
}
