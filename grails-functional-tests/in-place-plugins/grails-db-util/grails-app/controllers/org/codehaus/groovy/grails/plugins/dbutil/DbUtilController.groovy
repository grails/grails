package org.codehaus.groovy.grails.plugins.dbutil

import java.sql.*
import org.apache.commons.dbcp.*
import org.apache.commons.dbutils.handlers.MapListHandler

class DbUtilController {

  BasicDataSource dataSource
  /* Column metadata...
      COLUMN_NAME String => column name
      DATA_TYPE int => SQL type from java.sql.Types
      TYPE_NAME String => Data source dependent type name, for a UDT the type name is fully qualified
      COLUMN_SIZE int => column size.
      BUFFER_LENGTH is not used.
      DECIMAL_DIGITS int => the number of fractional digits. Null is returned for data types where DECIMAL_DIGITS is not applicable.
      NUM_PREC_RADIX int => Radix (typically either 10 or 2)
      NULLABLE int => is NULL allowed.
      columnNoNulls - might not allow NULL values
      columnNullable - definitely allows NULL values
      columnNullableUnknown - nullability unknown
      REMARKS String => comment describing column (may be null)
      COLUMN_DEF String => default value for the column, which should be interpreted as a string when the value is enclosed in single quotes (may be null)
      SQL_DATA_TYPE int => unused
      SQL_DATETIME_SUB int => unused
      CHAR_OCTET_LENGTH int => for char types the maximum number of bytes in the column
      ORDINAL_POSITION int => index of column in table (starting at 1)
      IS_NULLABLE String => ISO rules are used to determine the nullability for a column.
      YES --- if the parameter can include NULLs
      NO --- if the parameter cannot include NULLs
      empty string --- if the nullability for the parameter is unknown
      SCOPE_CATLOG String => catalog of table that is the scope of a reference attribute (null if DATA_TYPE isn't REF)
      SCOPE_SCHEMA String => schema of table that is the scope of a reference attribute (null if the DATA_TYPE isn't REF)
      SCOPE_TABLE String => table name that this the scope of a reference attribure (null if the DATA_TYPE isn't REF)
      SOURCE_DATA_TYPE short => source type of a distinct type or user-generated Ref type, SQL type from java.sql.Types (null if DATA_TYPE isn't DISTINCT or user-generated REF)
      IS_AUTOINCREMENT String => Indicates whether this column is auto incremented
  */
  def index = {redirect(action: data, params: params)}

  // Display database metadata
  def info = {
    def schemas = []
    def tables = []
    def columns = [:]


    Connection conn = dataSource.getConnection()
    def rs = conn.getMetaData()

    // Get list of tables
    def tmd = rs.getTables(null, null, null, null)
    while (tmd.next()) {
      if (tmd.getString("TABLE_TYPE") == "TABLE") {
        schemas << tmd.getString("TABLE_SCHEM")
        tables << tmd.getString("TABLE_NAME")
      }
    }

    // Get list of columns for each table
    def tableColMap = [:]
    tables.each {tableName ->
      def ResultSet colrs = rs.getColumns(null, null, tableName, null)
      def tableCols = [:]
      def isFirstRow = true
      def colLabels = []
      while (colrs.next()) {
        if (isFirstRow) {
          // This should be an arraylist of uppercase column names
          colLabels = colrs.getMetaData().columnMetaData.columnLabel*.toUpperCase()
          isFirstRow = false
        }

        // Check each that each metadata column exists in the result set and assign it to the map
        def colParamMap = [:]
        if (colLabels.contains("TYPE_NAME"))
          colParamMap["TYPE_NAME"] = colrs.getString("TYPE_NAME")

        if (colLabels.contains("COLUMN_SIZE"))
          colParamMap["COLUMN_SIZE"] = colrs.getString("COLUMN_SIZE")

        if (colLabels.contains("DECIMAL_DIGITS"))
          colParamMap["DECIMAL_DIGITS"] = colrs.getString("DECIMAL_DIGITS")

        if (colLabels.contains("DEFAULT_VALUE"))
          colParamMap["DEFAULT_VALUE"] = colrs.getString("COLUMN_DEF")

        if (colLabels.contains("IS_NULLABLE"))
          colParamMap["IS_NULLABLE"] = colrs.getString("IS_NULLABLE")

        if (colLabels.contains("IS_AUTOINCREMENT"))
          colParamMap["IS_AUTOINCREMENT"] = colrs.getString("IS_AUTOINCREMENT")

        // If the "COLUMN_NAME" column doesn't exist in the resultset we will get an exception which is fine
        // since the operation depends on this column
        tableCols[colrs.getString("COLUMN_NAME")] = colParamMap
      }
      // done w/ table
      tableColMap[tableName] = tableCols
    }

    conn.close()

    def dbModel = [tableList: tables.sort(), schemaList: schemas.unique().sort(), columnList: tableColMap]
    render(view: "info", model: dbModel)
  }

  // Display dump of database tables
  def data = {
    def tables = []
    def data = [:]

    Connection conn = dataSource.getConnection()
    def rs = conn.getMetaData()

    // Get list of tables
    def tmd = rs.getTables(null, null, null, null)
    while (tmd.next()) {
      if (tmd.getString("TABLE_TYPE") == "TABLE") {
        tables << tmd.getString("TABLE_NAME")
      }
    }

    // Get list of columns for each table
    def tableColMap = [:]
    tables.each {tableName ->
      def cmd = rs.getColumns(null, null, tableName, null)
      def tableCols = []
      while (cmd.next()) {
        tableCols.add(cmd.getString("COLUMN_NAME"))
      }
      // done w/ table
      tableColMap[tableName] = tableCols
    }

    // get the data
    def dataRow = [:]
    tables.sort().each {tableName ->
      def statement = conn.createStatement()
      statement.execute("select * from ${tableName}")
      def dataRs = statement.getResultSet()
      // get data for each column
      while (dataRs.next()) {
        def rowData = []
        tableColMap[tableName].sort().each {colName ->
          rowData << dataRs.getString(colName)
        }
        if (dataRow[tableName] == null)
          dataRow[tableName] = []

        dataRow[tableName] << rowData
      }
    }

    conn.close()

    def dbModel = [tableList: tables.sort(), dataList: dataRow, columnList: tableColMap]
    render(view: "data", model: dbModel)
  }

  def sql = {
    if (!params.sqlText)
      return
    def sql = params.sqlText?.trim()
    def retList
    Connection conn = dataSource.getConnection()
    Statement stmt = conn.createStatement()
    ResultSet rs
    if (sql[0..5].equalsIgnoreCase("SELECT")) {
      rs = stmt.executeQuery(sql)
      MapListHandler handler = new MapListHandler()
      retList = handler.handle(rs)
    }
    else {
      try {
        def cnt = stmt.executeUpdate(sql)
        retList = ["Rows affected": cnt]
      }
      catch (SQLException e) {
        retList = ["The following error occurred": e.toString()]
      }
    }
    conn.commit();
    conn.close()
    render(view: "sql", model: [dataList: retList, sqlText: sql])
  }
  
  /**
   * Custom action for the 'test-plugin' functional tests.
   */
  def testWithPluginLayout = {}
}
