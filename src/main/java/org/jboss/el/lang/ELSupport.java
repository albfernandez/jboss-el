/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jboss.el.lang;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.el.ELException;
import jakarta.el.PropertyNotFoundException;

import org.jboss.el.util.MessageFactory;


/**
 * A helper class that implements the EL Specification
 * 
 * @author Jacob Hookom [jacob@hookom.net]
 * @version $Change: 181177 $$DateTime: 2001/06/26 08:45:09 $$Author: markt $
 */
public class ELSupport {
    // private final static ELSupport REF = new ELSupport();

    private final static Long ZERO = Long.valueOf(0L);

    public final static void throwUnhandled(Object base, Object property)
            throws ELException {
        if (base == null) {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled.null", property));
        } else {
            throw new PropertyNotFoundException(MessageFactory.get(
                    "error.resolver.unhandled", base.getClass(), property));
        }
    }

    /**
     * Compare two objects (first coerce, then compare)
     * @param obj0 left hand
     * @param obj1 right hand
     * @return     a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * @throws ELException throws when invalid
     */
    @SuppressWarnings("unchecked")
	public final static int compare(final Object obj0, final Object obj1)
            throws ELException {
        if (obj0 == obj1 || equals(obj0, obj1)) {
            return 0;
        }
        if (isBigDecimalOp(obj0, obj1)) {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0, BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1, BigDecimal.class);
            return bd0.compareTo(bd1);
        }
        if (isDoubleOp(obj0, obj1)) {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.compareTo(d1);
        }
        if (isBigIntegerOp(obj0, obj1)) {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0, BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1, BigInteger.class);
            return bi0.compareTo(bi1);
        }
        if (isLongOp(obj0, obj1)) {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.compareTo(l1);
        }
        if (obj0 instanceof String || obj1 instanceof String) {
            return coerceToString(obj0).compareTo(coerceToString(obj1));
        }
        if (obj0 instanceof Comparable) {
            return (obj1 != null) ? ((Comparable) obj0).compareTo(obj1) : 1;
        }
        if (obj1 instanceof Comparable) {
            return (obj0 != null) ? -((Comparable) obj1).compareTo(obj0) : -1;
        }
        throw new ELException(MessageFactory.get("error.compare", obj0, obj1));
    }

    /**
     * Compares to objects (first coerce, then compare)
     * @param obj0 left hand
     * @param obj1 right hand
     * @return true if both objects are equals 
     * @throws ELException throws when invalid
     */
    public final static boolean equals(final Object obj0, final Object obj1)
            throws ELException {
        if (obj0 == obj1) {
            return true;
        } else if (obj0 == null || obj1 == null) {
            return false;
        } else if (obj0 instanceof Boolean || obj1 instanceof Boolean) {
            return coerceToBoolean(obj0).equals(coerceToBoolean(obj1));
        } else if (obj0.getClass().isEnum()) {
            return obj0.equals(coerceToEnum(obj1, obj0.getClass()));
        } else if (obj1.getClass().isEnum()) {
            return obj1.equals(coerceToEnum(obj0, obj1.getClass()));
        } else if (obj0 instanceof String || obj1 instanceof String) {
            int lexCompare = coerceToString(obj0).compareTo(coerceToString(obj1));
            return (lexCompare == 0) ? true : false;
        }
        if (isBigDecimalOp(obj0, obj1)) {
            BigDecimal bd0 = (BigDecimal) coerceToNumber(obj0, BigDecimal.class);
            BigDecimal bd1 = (BigDecimal) coerceToNumber(obj1, BigDecimal.class);
            return bd0.equals(bd1);
        }
        if (isDoubleOp(obj0, obj1)) {
            Double d0 = (Double) coerceToNumber(obj0, Double.class);
            Double d1 = (Double) coerceToNumber(obj1, Double.class);
            return d0.equals(d1);
        }
        if (isBigIntegerOp(obj0, obj1)) {
            BigInteger bi0 = (BigInteger) coerceToNumber(obj0, BigInteger.class);
            BigInteger bi1 = (BigInteger) coerceToNumber(obj1, BigInteger.class);
            return bi0.equals(bi1);
        }
        if (isLongOp(obj0, obj1)) {
            Long l0 = (Long) coerceToNumber(obj0, Long.class);
            Long l1 = (Long) coerceToNumber(obj1, Long.class);
            return l0.equals(l1);
        } else {
            return obj0.equals(obj1);
        }
    }
    
    /**
     * Convert and object to enum type
     * @param obj The object to convert
     * @param type the type to obtain.
     * @return the object converted to enum
     */
    @SuppressWarnings("unchecked")
	public final static Enum coerceToEnum(final Object obj, Class type) {
        if (obj == null || "".equals(obj)) {
            return null;
        }
        if (obj.getClass().isEnum()) {
            return (Enum<?>) obj;
        }
        return Enum.valueOf(type, obj.toString());
    }

    /**
     * Converts and object to boolean
     * @param obj The object to convert.
     * @return The boolean value of the object
     * @throws IllegalArgumentException if the object cannot be converted to boolean
     */
    public final static Boolean coerceToBoolean(final Object obj)
            throws IllegalArgumentException {
        if (obj == null || "".equals(obj)) {
            return Boolean.FALSE;
        }
        if (obj instanceof Boolean || obj.getClass() == Boolean.TYPE) {
            return (Boolean) obj;
        }
        if (obj instanceof String) {
            return Boolean.valueOf((String) obj);
        }

        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                obj, obj.getClass(), Boolean.class));
    }

    public final static Character coerceToCharacter(final Object obj)
            throws IllegalArgumentException {
        if (obj == null || "".equals(obj)) {
            return Character.valueOf((char) 0);
        }
        if (obj instanceof String) {
            return Character.valueOf(((String) obj).charAt(0));
        }
        if (ELArithmetic.isNumber(obj)) {
            return Character.valueOf((char) ((Number) obj).shortValue());
        }
        Class<?> objType = obj.getClass();
        if (obj instanceof Character || objType == Character.TYPE) {
            return (Character) obj;
        }

        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                obj, objType, Character.class));
    }

    public final static Number coerceToNumber(final Object obj) {
        if (obj == null) {
            return ZERO;
        } else if (obj instanceof Number) {
            return (Number) obj;
        } else {
            String str = coerceToString(obj);
            if (isStringFloat(str)) {
                return toFloat(str);
            } else {
                return toNumber(str);
            }
        }
    }

    protected final static Number coerceToNumber(final Number number,
            final Class<?> type) throws IllegalArgumentException {
    	 if (type.isInstance(number)) {
    		return number;
    	 }
        if (Long.TYPE == type || Long.class.equals(type)) {
            return Long.valueOf(number.longValue());
        }
        if (Double.TYPE == type || Double.class.equals(type)) {
            return Double.valueOf(number.doubleValue());
        }
        if (Integer.TYPE == type || Integer.class.equals(type)) {
            return Integer.valueOf(number.intValue());
        }
        if (BigInteger.class.equals(type)) {
            if (number instanceof BigDecimal) {
                return ((BigDecimal) number).toBigInteger();
            }
            return BigInteger.valueOf(number.longValue());
        }
        if (BigDecimal.class.equals(type)) {
            if (number instanceof BigInteger) {
                return new BigDecimal((BigInteger) number);
            }
            return new BigDecimal(number.doubleValue());
        }
        if (Byte.TYPE == type || Byte.class.equals(type)) {
            return Byte.valueOf(number.byteValue());
        }
        if (Short.TYPE == type || Short.class.equals(type)) {
            return Short.valueOf(number.shortValue());
        }
        if (Float.TYPE == type || Float.class.equals(type)) {
            return Float.valueOf(number.floatValue());
        }

        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                number, number.getClass(), type));
    }

    public final static Number coerceToNumber(final Object obj, final Class<?> type)
            throws IllegalArgumentException {
		if (obj == null || "".equals(obj)) {
			if (type == Number.class) {
				return null;
			}
			else {
				return coerceToNumber(ZERO, type);
			}
		}

        if (obj instanceof String) {
            return coerceToNumber((String) obj, type);
        }
        if (ELArithmetic.isNumber(obj)) {
            return coerceToNumber((Number) obj, type);
        }

        Class<?> objType = obj.getClass();
        if (Character.class.equals(objType) || Character.TYPE == objType) {
            return coerceToNumber(Short.valueOf((short) ((Character) obj)
                    .charValue()), type);
        }

        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                obj, objType, type));
    }

    protected final static Number coerceToNumber(final String val,
            final Class<?> type) throws IllegalArgumentException {
        if (Long.TYPE == type || Long.class.equals(type)) {
            return Long.valueOf(val);
        }
        if (Integer.TYPE == type || Integer.class.equals(type)) {
            return Integer.valueOf(val);
        }
        if (Double.TYPE == type || Double.class.equals(type)) {
            return Double.valueOf(val);
        }
        if (BigInteger.class.equals(type)) {
            return new BigInteger(val);
        }
        if (BigDecimal.class.equals(type)) {
            return new BigDecimal(val);
        }
        if (Byte.TYPE == type || Byte.class.equals(type)) {
            return Byte.valueOf(val);
        }
        if (Short.TYPE == type || Short.class.equals(type)) {
            return Short.valueOf(val);
        }
        if (Float.TYPE == type || Float.class.equals(type)) {
            return Float.valueOf(val);
        }

        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                val, String.class, type));
    }

    /**
     * Converts the object to String, empty String if obj is null
     * @param obj object to convert
     * @return object converted to String
     */
    public final static String coerceToString(final Object obj) {
        if (obj == null) {
            return "";
        } else if (obj instanceof String) {
            return (String) obj;
        } else if (obj instanceof Enum) {
            return ((Enum<?>) obj).name();
        } else {
            return obj.toString();
        }
    }

    public final static Object coerceToType(final Object obj, final Class<?> type)
            throws IllegalArgumentException {
        if (type == null || Object.class.equals(type)) {
            return obj;
        }
        
        if (obj!=null && obj.getClass().equals(type)) {   
            return obj;
        }
        else if (obj != null && type.isInstance(obj)) {
        	return obj;
        }
        if (String.class.equals(type)) {
            return coerceToString(obj);
        }
        if (ELArithmetic.isNumberType(type)) {
            return coerceToNumber(obj, type);
        }
        if (Character.class.equals(type) || Character.TYPE == type) {
            return coerceToCharacter(obj);
        }
        if (Boolean.class.equals(type) || Boolean.TYPE == type) {
            return coerceToBoolean(obj);
        }

        if (type.isEnum()) {
            return coerceToEnum(obj, type);
        }

        // new to spec
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            if ("".equals(obj)) {
                return null;
            }
            PropertyEditor editor = PropertyEditorManager.findEditor(type);
            if (editor != null) {
                editor.setAsText((String) obj);
                return editor.getValue();
            }
        }
        throw new IllegalArgumentException(MessageFactory.get("error.convert",
                obj, obj.getClass(), type));
    }

    /**
     * @param obj Array of objects to check for nulls
     * @return true if any object in the array is null
     */
    public final static boolean containsNulls(final Object[] obj) {
        for (int i = 0; i < obj.length; i++) {
            if (obj[0] == null) {
                return true;
            }
        }
        return false;
    }

    public final static boolean isBigDecimalOp(final Object obj0,
            final Object obj1) {
        return (obj0 instanceof BigDecimal || obj1 instanceof BigDecimal);
    }

    public final static boolean isBigIntegerOp(final Object obj0,
            final Object obj1) {
        return (obj0 instanceof BigInteger || obj1 instanceof BigInteger);
    }

    public final static boolean isDoubleOp(final Object obj0, final Object obj1) {
        return (obj0 instanceof Double
                || obj1 instanceof Double
                || obj0 instanceof Float
                || obj1 instanceof Float
                || (obj0 != null && (Double.TYPE == obj0.getClass() || Float.TYPE == obj0
                        .getClass())) || (obj1 != null && (Double.TYPE == obj1
                .getClass() || Float.TYPE == obj1.getClass())));
    }

    public final static boolean isDoubleStringOp(final Object obj0,
            final Object obj1) {
        return (isDoubleOp(obj0, obj1)
                || (obj0 instanceof String && isStringFloat((String) obj0)) || (obj1 instanceof String && isStringFloat((String) obj1)));
    }

    public final static boolean isLongOp(final Object obj0, final Object obj1) {
        return (obj0 instanceof Long
                || obj1 instanceof Long
                || obj0 instanceof Integer
                || obj1 instanceof Integer
                || obj0 instanceof Character
                || obj1 instanceof Character
                || obj0 instanceof Short
                || obj1 instanceof Short
                || obj0 instanceof Byte
                || obj1 instanceof Byte
                || (obj0 != null && (Long.TYPE == obj0.getClass()
                        || Integer.TYPE == obj0.getClass()
                        || Character.TYPE == obj0.getClass()
                        || Short.TYPE == obj0.getClass() || Byte.TYPE == obj0
                        .getClass())) || (obj0 != null && (Long.TYPE == obj0
                .getClass()
                || Integer.TYPE == obj0.getClass()
                || Character.TYPE == obj0.getClass()
                || Short.TYPE == obj0.getClass() || Byte.TYPE == obj0
                .getClass())));
    }

    public final static boolean isStringFloat(final String str) {
        int len = str.length();
        if (len > 1) {
            for (int i = 0; i < len; i++) {
                switch (str.charAt(i)) {
                case 'E':
                    return true;
                case 'e':
                    return true;
                case '.':
                    return true;
                default: //nop
                }
            }
        }
        return false;
    }

    public final static Number toFloat(final String value) {
        try {
            if (Double.parseDouble(value) > Double.MAX_VALUE) {
                return new BigDecimal(value);
            } else {
                return Double.valueOf(value);
            }
        } catch (NumberFormatException e0) {
            return new BigDecimal(value);
        }
    }

    public final static Number toNumber(final String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e0) {
            try {
                return Long.valueOf(value);
            } catch (NumberFormatException e1) {
                return new BigInteger(value);
            }
        }
    }
}
