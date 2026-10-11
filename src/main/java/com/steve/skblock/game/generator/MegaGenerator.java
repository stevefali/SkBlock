package com.steve.skblock.game.generator;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class MegaGenerator<T> {

    private final NavigableMap<Integer, T> entries;
    private final int denominator;
    private final Map<T, String> percentages;

    public MegaGenerator(NavigableMap<Integer, T> entries, int denominator, Map<T, String> percentages) {
        this.entries = entries;
        this.denominator = denominator;
        this.percentages = percentages;
    }

    public T getRandomEntry() {
        int rand = ThreadLocalRandom.current().nextInt(denominator);
        return entries.higherEntry(rand).getValue();
    }

    public Map<T, String> getPercentages() {
        return this.percentages;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static final class Builder<T> {
        private final NavigableMap<Integer, T> entries = new TreeMap<>();
        private int runningTotal = 0;
        private final LinkedHashMap<T, Integer> frequencies = new LinkedHashMap<>();

        public Builder<T> add(T value, int frequency) {
            if (frequency <= 0) {
                throw new IllegalArgumentException("Numerator must be greater than 0");
            }

            runningTotal += frequency;
            entries.put(runningTotal, value);
            frequencies.put(value, frequency);
            return this;
        }

        public MegaGenerator<T> build() {
            if (entries.isEmpty()) {
                throw new IllegalStateException("MegaGenerator is empty!");
            }
            Map<T, String> percentages = new LinkedHashMap<>();
            for (Map.Entry<T, Integer> entry : frequencies.entrySet()) {
                double percentage = ((double) entry.getValue() / runningTotal) * 100;
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                percentages.put(entry.getKey(), decimalFormat.format(percentage));
            }

            return new MegaGenerator<>(entries, runningTotal, Collections.unmodifiableMap(percentages));
        }
    }

}
