// The overall database change log
databaseChangeLog(logicalFilePath:'site-autobase') { 
  include('./migrations/Rhyolight/PluginPortalCommentsMigration.groovy')
}
