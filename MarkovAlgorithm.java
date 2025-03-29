import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class MarkovAlgorithm {
    private static class Rule {
        String left;
        String right;
        @SuppressWarnings("unused")
        String comment;

        Rule(String left, String right, String comment) {
            this.left = left;
            this.right = right;
            this.comment = comment;
        }
    }

    private static String readSection(DataInputStream dis) throws IOException {
        // Read length (4 bytes, little endian)
        byte[] lengthBytes = new byte[4];
        dis.readFully(lengthBytes);
        int length = ByteBuffer.wrap(lengthBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

        // Read the section content
        byte[] content = new byte[length];
        dis.readFully(content);
        String result = new String(content, "ASCII");
        
        // Remove 0D0A from input string if present
        if (result.endsWith("\r\n")) {
            result = result.substring(0, result.length() - 2);
        }
        
        return result;
    }

    private static List<Rule> parseRules(String rulesSection) {
        List<Rule> rules = new ArrayList<>();
        String[] ruleStrings = rulesSection.split("\r\n");
        
        for (String ruleString : ruleStrings) {
            if (ruleString.trim().isEmpty()) continue;
            
            // Split by tabs while preserving empty fields
            String[] parts = ruleString.split("\t", -1);
            
            if (parts.length >= 2) {
                String left = parts[0];
                String right = parts[1];
                String comment = parts.length > 2 ? parts[2] : "";
                rules.add(new Rule(left, right, comment));
            }
        }
        return rules;
    }

    private static String applyRules(String input, List<Rule> rules, boolean verbose) {
        String current = input;
        boolean changed;
        final int MAX_STRING_LENGTH = 32768;
        
        if (verbose) {
            System.out.println("Initial: " + current);
        }
        
        do {
            changed = false;
            for (Rule rule : rules) {
                if (current.contains(rule.left)) {
                    String newString = current.replaceFirst(Pattern.quote(rule.left), rule.right);
                    if (!newString.equals(current)) {
                        // Check if the new string would exceed the maximum length
                        if (newString.length() > MAX_STRING_LENGTH) {
                            return current;
                        }
                        current = newString;
                        changed = true;
                        
                        if (verbose) {
                            System.out.println("Applied rule: " + rule.left + " -> " + rule.right);
                            System.out.println("Result: " + current);
                        }
                        
                        if (rule.right.contains(".")) {
                            return current;
                        }
                        break;
                    }
                }
            }
        } while (changed);
        
        return current;
    }

    public static void main(String[] args) throws IOException {
        boolean verbose = false;
        String filename = null;
        
        // Parse command line arguments
        for (String arg : args) {
            if (arg.equals("-v") || arg.equals("--verbose")) {
                verbose = true;
            } else {
                filename = arg;
            }
        }
        
        if (filename == null) {
            System.err.println("Usage: java MarkovAlgorithm [--verbose|-v] <filename>");
            System.exit(1);
        }
        
        FileInputStream fis = new FileInputStream(filename);
        DataInputStream dis = new DataInputStream(fis);
        
        // Read and ignore condition section
        readSection(dis);
        
        // Read input string
        String input = readSection(dis);
        
        // Read and parse rules
        String rulesSection = readSection(dis);
        List<Rule> rules = parseRules(rulesSection);
        
        // Apply rules and output result
        String result = applyRules(input, rules, verbose);
        
        if (!verbose) {
            System.out.println(result);
        }
    }
} 