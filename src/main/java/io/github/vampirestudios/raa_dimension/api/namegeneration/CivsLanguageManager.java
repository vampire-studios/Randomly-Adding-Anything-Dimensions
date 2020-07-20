package io.github.vampirestudios.raa_dimension.api.namegeneration;

import io.github.vampirestudios.raa_core.api.name_generation.Language;
import io.github.vampirestudios.raa_dimension.api.namegeneration.civs.EnglishCivs;
import io.github.vampirestudios.raa_dimension.api.namegeneration.civs.FrenchCivs;

public class CivsLanguageManager {

    public static final String CIVS_NAME = "civs_name";

    public static void init() {
        Language.ENGLISH.addNameGenerator(CIVS_NAME, new EnglishCivs());
        Language.FRENCH.addNameGenerator(CIVS_NAME, new FrenchCivs());
    }
}
