package refactula.design.patterns.creational.builder;

public interface ReportBuilder {

    default void addObjectValue(Object o) {
        addValue(String.valueOf(o));
    }

    void addValue(String value);

    void endRecord();

}
