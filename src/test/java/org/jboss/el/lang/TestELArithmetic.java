package org.jboss.el.lang;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

public class TestELArithmetic {
	
	public TestELArithmetic() {
		super();
	}
	
    private <T> void assertCoerceToBigDecimalSame(Object val){
        Object coerced = ELArithmetic.BIGDECIMAL.coerce(val);
        assertEquals(val, coerced);
        assertSame(val, coerced);
    }
    private <T> void assertCoerceToBigDecimalSameNumber(Number val){
        Object coerced = ELArithmetic.BIGDECIMAL.coerce(val);
        assertEquals(val, coerced);
        assertSame(val, coerced);
    }

    @Test
    public void testCoerceObjectBigDecimal(){
        assertCoerceToBigDecimalSame(new BigDecimal("3434"));
    }
    @Test
    public void testCoerceObjectMyBigDecimal(){
        assertCoerceToBigDecimalSame(new MyBigDecimal("3434"));
    }
    @Test
    public void testCoerceNumberBigDecimal(){
        assertCoerceToBigDecimalSameNumber(new BigDecimal("3434"));
    }
    @Test
    public void testCoerceNumberMyBigDecimal(){
        assertCoerceToBigDecimalSameNumber(new MyBigDecimal("3434"));
    }
    @Test
    public void testCoerceLongToBigDecimal(){
        Number num = ELArithmetic.BIGDECIMAL.coerce((Object)5L);
        assertTrue(num instanceof BigDecimal, "should be an instanceof BigDecimal but is of class "+num.getClass());
        assertEquals(5L, num.longValue());
    }

    private void assertNumberType(Class<?> type){
        if (!ELArithmetic.isNumberType(type)) {
            throw new AssertionError(type.getName()+" is number type but ELArithmetic missed it.");
        }
    }
    // Tests for ELSupport.isNumberType
    @Test
    public void testIsNumberType(){
        assertFalse(ELArithmetic.isNumberType(Object.class));
        assertFalse(ELArithmetic.isNumberType(String.class));

        assertNumberType(Long.class);
        assertNumberType(Long.TYPE);

        assertNumberType(BigDecimal.class);
        assertNumberType(BigInteger.class);
        assertNumberType(Number.class);

        assertNumberType(MyBigDecimal.class);
        assertNumberType(MyNumber.class);
    }
}
