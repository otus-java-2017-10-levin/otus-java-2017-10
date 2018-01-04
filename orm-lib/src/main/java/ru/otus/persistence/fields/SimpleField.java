package ru.otus.persistence.fields;

/**
 * Contains information of the field of several types:
 * <ul>
 *     <li>any Java   primitive   type</li>
 *     <li>any   primitive   wrapper   type</li>
 *     <li>java.lang.String</li>
 *     <li>java.util.Date</li>
 *     <li>java.sql.Date</li>
 *     <li>java.math.BigDecimal</li>
 *     <li>java.math.BigInteger</li>
 * </ul>
 *
 */
public interface SimpleField<T> extends Field<T> {

}
