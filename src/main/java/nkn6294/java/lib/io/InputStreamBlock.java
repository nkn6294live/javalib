package nkn6294.java.lib.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;

import nkn6294.java.lib.collection.CollectionUtil;
import nkn6294.java.lib.delegate.Action;
import nkn6294.java.lib.delegate.Function;
import nkn6294.java.lib.delegate.Predicate;
import nkn6294.java.lib.document.DocumentReader;

public class InputStreamBlock implements DocumentReader {

    public final Character[] DELIMETER_WORD = {
        ' ', '\n', '\t', (char) 13, (char) 65279, //',', ':', '.', '-', '…'
    };
    public final Character[] DELIMETER_PARAGRAPH = {(char) 10, (char) 13};

    public InputStreamBlock(Reader reader) {
        this.reader = reader;
    }

    public InputStreamBlock(InputStream stream) {
        this.reader = new BufferedReader(new InputStreamReader(stream));
    }

    public int next() throws IOException {
        return this.reader.read();
    }

    public String nextElement(Predicate<Character> predicate) throws IOException {
        if (predicate == null) {
            return null;
        }
        int next = -1;
        do {
            if ((next = reader.read()) < 0) {
                return null;
            }
        } while (!predicate.isInvalid((char) next));
        StringWriter writer = new StringWriter();
        do {
            writer.write(next);
            if ((next = reader.read()) < 0) {
                break;
            }
        } while (predicate.isInvalid((char) next));
        return writer.toString();
    }

    public String nextElement() throws IOException {
        return this.nextElement(new Predicate<Character>() {

            @Override
            public boolean isInvalid(Character... values) {
                if (values.length > 0) {
                    return Character.isLetterOrDigit(values[0]);
                }
                return true;
            }
        });
    }

    @Override
    public String nextWord() throws IOException {
        return this.nextElement(DELIMETER_WORD);
    }

    public String nextElement(final Character[] delimeterChar) throws IOException {
        return this.nextElement(new Predicate<Character>() {

            @Override
            public boolean isInvalid(Character... values) {
                if (values.length > 0) {
                    return !CollectionUtil.Contains(delimeterChar, values[0]);
                }
                return true;
            }
        }
        );
    }

    public String nextElementAdvance(final Character[] delimeterChar, final Action<Character> action) throws IOException {
        return this.nextElement(new Predicate<Character>() {

            @Override
            public boolean isInvalid(Character... values) {
                if (values.length > 0) {
                    if (CollectionUtil.Contains(delimeterChar, values[0])) {
                        if (action != null) {
                            action.action(values[0]);
                        }
                        return false;
                    }
                    return true;
                }
                return true;
            }
        }
        );
    }

    @Override
    public String nextParagraph() throws IOException {
        return this.nextElement(DELIMETER_PARAGRAPH);
    }

    @Override
    public String nextParagraph(Function<String, String> actionWord) throws Exception {
        if (actionWord == null) {
            return this.nextParagraph();
        }
        ActionFlag actionFlag = new ActionFlag();
        StringWriter writer = new StringWriter();
        String word = null;
        while (true) {
            if (!actionFlag.getFlag()) {
                if ((word = this.nextElementAdvance(DELIMETER_WORD, actionFlag)) != null) {
                        String output = actionWord.action(word);
                        if (output != null) {
                            writer.write(output);
                            writer.write(actionFlag.getCurrentChar());
                        }
                } else {
                    return null;
                }
            } else {
                break;
            }
        }
        return writer.toString();
    }

    public Reader getReader() {
        return this.reader;
    }

    private final Reader reader;

    @Override
    public String nextTitle() {
        return "";
    }

    @Override
    public void close() throws Exception {
        this.reader.close();
    }
    
    private class ActionFlag implements Action<Character> {

        @Override
        public void action(Character... values) {
            if (values.length > 0) {
                this.flag = CollectionUtil.Contains(DELIMETER_PARAGRAPH, values[0]);
                this.currentChar = values[0];
            }
        }

        public boolean getFlag() {
            return this.flag;
        }

        public char getCurrentChar() {
            return this.currentChar;
        }
        private boolean flag = false;
        private Character currentChar = null;
    }
}
