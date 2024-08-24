package exel.userinterface.input.api;

public interface InputHandler {
    /**
     * Reads a line of text from the input.
     * @return The line as a String.
     */
    String readLine();

    /**
     * Reads an integer value from the input.
     * Ensures the input is valid and prompts again if not.
     * @return The integer value.
     */
    int readInt();


    int readIntRange(int min, int max);

    /**
     * Closes the input handler and releases any resources associated with it.
     */
    void close();
}
