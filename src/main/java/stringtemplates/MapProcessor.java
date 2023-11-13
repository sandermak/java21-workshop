package stringtemplates;

import java.util.Map;

public class MapProcessor {
    // TODO: Implement a Processor
    // Construct a Processor that creates a map containing keys and values
    // as show in the syntax below. Throw an NPE when a null value is being
    // inserted. You can assume well-formed input is provided for this exercise.
    static StringTemplate.Processor<Map<String, Object>, NullPointerException> MAP = null;

    public static void main(String[] args) {
        Map<String, Object> map = MAP."""
                key1 -> \{ 1 + 2 }
                second_key -> \{ new Object() }
                3 -> \{2.2d}
                """;

        // (Tip: you can use the RAW processor to inspect a template)

        System.out.println(map);
    }
}
