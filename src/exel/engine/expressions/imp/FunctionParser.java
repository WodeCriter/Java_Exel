package exel.engine.expressions.imp;

import exel.engine.expressions.api.Expression;
import exel.engine.expressions.imp.Math.*;
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
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
                    return new PlusExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    MINUS
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
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
                    {
                        throw new IllegalArgumentException("Invalid number of arguments for MINUS function. Expected 1, but got " + arguments.size());
                    }

                    //structure is good. parse arguments
                    Expression exp = parseExpression(arguments.get(0).trim());

                    // more validations on the expected argument types
                    if (!exp.getFunctionResultType().equals(CellType.NUMERIC))
                    {
                        throw new IllegalArgumentException("Invalid argument types for MINUS function. Expected NUMERIC, but got " + exp.getFunctionResultType());
                    }

                    return new AbsExpression(exp);
                }
            },
    DIVIDE
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
                    return new DivideExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    MOD
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
                    return new ModExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    POW
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
                    return new PowExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },
    TIMES
            {
                @Override
                public Expression parse(List<String> arguments)
                {
                    AbstractMap.SimpleEntry<Expression, Expression> parsedExpressions = areArgumentsValid(arguments);
                    return new TimesExpression(parsedExpressions.getKey(), parsedExpressions.getValue());
                }
            },


    /*UPPER_CASE {
        @Override
        public Expression parse(List<String> arguments) {
            // validations of the function. it should have exactly one argument
            if (arguments.size() != 1) {
                throw new IllegalArgumentException("Invalid number of arguments for UPPER_CASE function. Expected 1, but got " + arguments.size());
            }

            // structure is good. parse arguments
            Expression arg = parseExpression(arguments.get(0).trim());

            // more validations on the expected argument types
            if (!arg.getFunctionResultType().equals(CellType.STRING)) {
                throw new IllegalArgumentException("Invalid argument types for UPPER_CASE function. Expected STRING, but got " + arg.getFunctionResultType());
            }

            // all is good. create the relevant function instance
            return new UpperCaseExpression(arg);
        }
    } **/;

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
            return FunctionParser.valueOf(functionName).parse(topLevelParts);
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

    public static List<String> getCellsInString(String input) {
        List<String> cellsList = null;

        if (input.startsWith("{") && input.endsWith("}"))
        {
            String functionContent = input.substring(1, input.length() - 1);
            cellsList = parseMainParts(functionContent);

            //remove all the non cell strings from the list
            cellsList.removeIf(topLevelPart -> !isStringACellCoordinate(topLevelPart));
        }
        else if (isStringACellCoordinate(input))
        {
            cellsList = new LinkedList<>();
            cellsList.add(input);
        }

        return cellsList;
    }

    private static Boolean isStringACellCoordinate(String input) {
        char[] strAsArr = input.toCharArray();

        if (!Character.isUpperCase(strAsArr[0]))
            return false;

        for (int i = 1; i < strAsArr.length; i++)
        {
            if (!Character.isDigit(strAsArr[i]))
                return false;
        }

        return true;
    }

    private static AbstractMap.SimpleEntry<Expression, Expression> areArgumentsValid(List<String> arguments)
    {
        // validations of the function. it should have exactly two arguments
        if (arguments.size() != 2)
        {
            throw new IllegalArgumentException("Invalid number of arguments for MINUS function. Expected 2, but got " + arguments.size());
        }

        //structure is good. parse arguments
        Expression left = parseExpression(arguments.get(0).trim());
        Expression right = parseExpression(arguments.get(1).trim());

        // more validations on the expected argument types
        if (!left.getFunctionResultType().equals(CellType.NUMERIC) || !right.getFunctionResultType().equals(CellType.NUMERIC))
            throw new IllegalArgumentException("Invalid argument types for MINUS function. " +
                    "Expected NUMERIC, but got " + left.getFunctionResultType() + " and " + right.getFunctionResultType());

        return new AbstractMap.SimpleEntry<>(left, right);
    }
}

