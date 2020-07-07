package io.github.vampirestudios.raa_dimension.api.namegeneration;

import io.github.vampirestudios.raa_core.api.name_generation.Language;
import io.github.vampirestudios.raa_dimension.api.namegeneration.dimensions.EnglishDimensions;
import io.github.vampirestudios.raa_dimension.api.namegeneration.dimensions.FrenchDimensions;
import io.github.vampirestudios.raa_dimension.api.namegeneration.dimensions.NorwegianDimensions;
import io.github.vampirestudios.raa_dimension.api.namegeneration.dimensions.SpanishDimensions;

public class DimensionLanguageManager {

    public static final String DIMENSION_NAME = "dimension_name";

    public static void init() {
        Language.ENGLISH.addNameGenerator(DIMENSION_NAME, new EnglishDimensions());
        Language.FRENCH.addNameGenerator(DIMENSION_NAME, new FrenchDimensions());
        Language.SPANISH.addNameGenerator(DIMENSION_NAME, new SpanishDimensions());
        Language.NORWEGIAN_BO.addNameGenerator(DIMENSION_NAME, new NorwegianDimensions());
    }
}
