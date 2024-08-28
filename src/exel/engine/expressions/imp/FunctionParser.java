package exel.engine.expressions.imp;

import exel.engine.expressions.api.Expression;
import exel.engine.expressions.imp.Math.*;
import exel.engine.expressions.imp.String.ConcatExpression;
import exel.engine.expressions.imp.String.SubExpression;
import exel.engine.spreadsheet.cell.api.CellType;

import java.util.*;

public enum FunctionParser
{
    IDENTITY
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    // validations of the function. it should have exactly one argument
                    if (arguments.size() != 1)
                        throw new IllegalArgumentException("Invalid number of arguments for IDENTITY function. Expected 1, but got " + arguments.size());

                    // all is good. create the relevant function instance
                    String actualValue = arguments.get(0).trim();
                    if (isBoolean(actualValue))
                        return new IdentityExpression(Boolean.parseBoolean(actualValue), CellType.BOOLEAN);
                    else if (isNumeric(actualValue))
                        return new IdentityExpression(Double.parseDouble(actualValue), CellType.NUMERIC);
                    else
                        return new IdentityExpression(actualValue, CellType.STRING);
                }

                private boolean isBoolean(String value)
                {
                    return "true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value);
                }

                private boolean isNumeric(String value)
                {
                    try
                    {
                        Double.parseDouble(value);
                        return true;
                    } catch (NumberFormatException e)
                    {
                        return false;
                    }
                }
            },
    PLUS
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "PLUS", CellType.NUMERIC);
                    return new PlusExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    MINUS
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "MINUS", CellType.NUMERIC);
                    // all is good. create the relevant function instance
                    return new MinusExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    ABS
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    // validations of the function. it should have exactly two arguments
                    if (arguments.size() != 1)
                        throw new IllegalArgumentException("Invalid number of arguments for ABS function. Expected 1, but got " + arguments.size());

                    //structure is good. parse arguments
                    Expression exp = parseExpression(arguments.get(0).trim());

                    // more validations on the expected argument types
                    if (!exp.getFunctionResultType().equals(CellType.NUMERIC))
                        throw new IllegalArgumentException("Invalid argument types for ABS function. Expected NUMERIC, but got " + exp.getFunctionResultType());

                    return new AbsExpression(exp);
                }
            },
    DIVIDE
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "DIVIDE", CellType.NUMERIC);
                    return new DivideExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    MOD
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "MOD", CellType.NUMERIC);
                    return new ModExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    POW
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "POW", CellType.NUMERIC);
                    return new PowExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    TIMES
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "TIMES", CellType.NUMERIC);
                    return new TimesExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    CONCAT
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = checkArgsAndParseExpressions(arguments, "CONCAT", CellType.STRING);
                    return new ConcatExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    SUB
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    // validations of the function. it should have exactly two arguments
                    if (arguments.size() != 3)
                        throw new IllegalArgumentException("Invalid number of arguments for SUB function. Expected 3, but got " + arguments.size());

                    //structure is good. parse arguments
                    Expression stringToCut = parseExpression(arguments.get(0).trim());
                    Expression startIndex = parseExpression(arguments.get(1).trim());
                    Expression endIndex = parseExpression(arguments.get(2).trim());

                    if (!stringToCut.getFunctionResultType().equals(CellType.STRING) || !startIndex.getFunctionResultType().equals(CellType.NUMERIC) || !endIndex.getFunctionResultType().equals(CellType.NUMERIC))
                        throw new IllegalArgumentException("Invalid argument types for SUB function. Expected String and 2 Numerics, but got " + stringToCut.getFunctionResultType() + ", " + startIndex.getFunctionResultType() + ", " + endIndex.getFunctionResultType());

                    return new SubExpression(stringToCut, startIndex, endIndex);
                }
            },
    REF {
        @Override
        public Expression parse(List<String> arguments)
        {
            if (arguments.size() != 1)
                throw new IllegalArgumentException("Invalid number of arguments for REF function. Expected 1, but got " + arguments.size());

            String coordinate = arguments.getFirst().trim();
            if (!isStringACellCoordinate(coordinate))
                throw new IllegalArgumentException("Invalid argument for REF function. Expected a valid cell reference, but got " + coordinate);

            return new RefExpression(coordinate);
        }
    };

    abstract public Expression parse(List<String> arguments);

    public static Expression parseExpression(String input)
    {
        if (input.startsWith("{") && input.endsWith("}"))
        {
            String functionContent = input.substring(1, input.length() - 1);
            List<String> topLevelParts = parseMainParts(functionContent);
            String functionName = topLevelParts.get(0).trim().toUpperCase();

            //remove the first element from the array
            topLevelParts.removeFirst();

            //Idea: topLevelParts now has either cells, functions, or numbers.
            //If it's a cell, it should be added to the "dependsOn" list given in the method.
            FunctionParser func;
            try {
                func = FunctionParser.valueOf(functionName);
            }
            //If you get an exception (probably from the enum) tell user the function did not exist
            catch (IllegalArgumentException e )
            {
                throw new IllegalArgumentException("Invalid function name: " + functionName);
            }

            return func.parse(topLevelParts);
        }

        // handle identity expression
        return FunctionParser.IDENTITY.parse(List.of(input.trim()));
    }

    private static List<String> parseMainParts(String input)
    {
        List<String> parts = new LinkedList<>();
        StringBuilder buffer = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (char c : input.toCharArray())
        {
            if (c == '{')
                stack.push(c);
            else if (c == '}')
                stack.pop();

            if (c == ',' && stack.isEmpty())
            {
                // If we are at a comma and the stack is empty, it's a separator for top-level parts
                parts.add(buffer.toString().trim());
                buffer.setLength(0); // Clear the buffer for the next part
            }
            else
                buffer.append(c);
        }

        // Add the last part
        if (buffer.length() > 0)
            parts.add(buffer.toString().trim());

        return parts;
    }

    public static List<String> getCellCordsInOriginalValue(String input) {
        List<String> cellsList = new LinkedList<>();

        if (isStringACellCoordinate(input))
            cellsList.add(input);
        else if (input.startsWith("{") && input.endsWith("}"))
        {
            String functionContent = input.substring(1, input.length() - 1);
            List<String> mainParts = parseMainParts(functionContent);
            mainParts.removeFirst(); //Removes function name

            for (String part : mainParts)
                cellsList.addAll(getCellCordsInOriginalValue(part));
        }
        return cellsList;
    }

    private static Boolean isStringACellCoordinate(String input) {
        char[] strAsArr = input.trim().toCharArray();

        if (strAsArr.length == 0 || !Character.isLetter(strAsArr[0]))
            return false;

        int i = 1;
        while (Character.isLetter(strAsArr[i]))
        {
            i++;
        }

        for (; i < strAsArr.length; i++)
        {
            if (!Character.isDigit(strAsArr[i]))
                return false;
        }

        return true;
    }

    private static AbstractMap.SimpleEntry<Expression, Expression> checkArgsAndParseExpressions
            (List<String> arguments, String funcName, CellType expectedArgsType)
    {
        // validations of the function. it should have exactly two arguments
        if (arguments.size() != 2)
            throw new IllegalArgumentException("Invalid number of arguments for " + funcName + " function. Expected 2, but got " + arguments.size());

        //structure is good. parse arguments
        Expression left = parseExpression(arguments.get(0).trim());
        Expression right = parseExpression(arguments.get(1).trim());

        CellType leftType = left.getFunctionResultType();
        CellType rightType = right.getFunctionResultType();

        // more validations on the expected argument types
        if ((!leftType.equals(expectedArgsType) && !leftType.equals(CellType.UNKNOWN)) ||
                (!rightType.equals(expectedArgsType) && !rightType.equals(CellType.UNKNOWN)))
            throw new IllegalArgumentException("Invalid argument types for " + funcName + " function. " +
                    "Expected " + expectedArgsType.toString() + ", but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());

        return new AbstractMap.SimpleEntry<>(left, right);
    }
}

