package com.g2one.gtunes

import java.sql.*
import org.hibernate.*
import org.hibernate.usertype.UserType

class MonetaryAmountUserType implements UserType {

    private static final SQL_TYPES = [ Types.NUMERIC, Types.VARCHAR ] as int[]

    public int[] sqlTypes() { SQL_TYPES }
    public Class returnedClass() { MonetaryAmount }
    public boolean equals(x, y) { x == y }
    public int hashCode(x) { x.hashCode() }
    public Object deepCopy(value) { value }
    public boolean isMutable() { false }

    Serializable disassemble(value)  { value }
    def assemble(Serializable cached, owner)  { cached  }
    def replace(original, target, owner) { original  } 


    public Object nullSafeGet(ResultSet resultSet,
                              String[] names,
                              Object owner)
            throws HibernateException, SQLException {
        if (resultSet.wasNull()) return null

        def value = resultSet.getBigDecimal(names[0])
        def currency = Currency.getInstance(resultSet.getString(names[1]))
        return new MonetaryAmount(value, currency)
    }

    void nullSafeSet(PreparedStatement statement,
                            Object amount,
                            int index) {

        if (amount == null) {
            statement.setNull(index, SQL_TYPES[index])
            statement.setNull(index + 1, SQL_TYPES[index + 1])
        }
        else {
            def currencyCode = amount.currency.currencyCode
            statement.setBigDecimal(index, amount.value)
            statement.setString(index + 1, currencyCode)
        }
    }

}
