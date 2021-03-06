/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.launcher.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public final class BundleUtils {

    private static final Logger logger = LoggerFactory.getLogger(BundleUtils.class);

    private static final String LABELS_BUNDLE = "org.terasology.launcher.bundle.LabelsBundle";
    private static final String MESSAGE_BUNDLE = "org.terasology.launcher.bundle.MessageBundle";
    private static final String URI_BUNDLE = "org.terasology.launcher.bundle.URIBundle";
    private static final String IMAGE_BUNDLE = "org.terasology.launcher.bundle.ImageBundle";
    private static final String FXML_BUNDLE = "org.terasology.launcher.bundle.FXMLBundle";

    private BundleUtils() {
    }

    public static String getLabel(String key) {
        return getLabel(Languages.getCurrentLocale(), key);
    }

    public static String getLabel(Locale locale, String key) {
        try {
            String label = ResourceBundle.getBundle(LABELS_BUNDLE, locale).getString(key);
            if (label.length() == 0) {
                throw new IllegalArgumentException();
            }
            return label;
        } catch (MissingResourceException | IllegalArgumentException e) {
            logger.error("Missing label translation! key={}, locale={}", key, locale);
            return ResourceBundle.getBundle(LABELS_BUNDLE, Languages.DEFAULT_LOCALE).getString(key);
        }
    }

    public static String getMessage(String key, Object... arguments) {
        String pattern;
        try {
            pattern = ResourceBundle.getBundle(MESSAGE_BUNDLE, Languages.getCurrentLocale()).getString(key);
            if (pattern.length() == 0) {
                throw new IllegalArgumentException();
            }
        } catch (MissingResourceException | IllegalArgumentException e) {
            logger.error("Missing message translation! key={}, locale={}", key, Languages.getCurrentLocale());
            pattern = ResourceBundle.getBundle(MESSAGE_BUNDLE, Languages.DEFAULT_LOCALE).getString(key);
        }
        final MessageFormat messageFormat = new MessageFormat(pattern, Languages.getCurrentLocale());
        return messageFormat.format(arguments, new StringBuffer(), null).toString();
    }

    public static URI getURI(String key) {
        final String uriStr = ResourceBundle.getBundle(URI_BUNDLE, Languages.getCurrentLocale()).getString(key);
        try {
            return new URI(uriStr);
        } catch (URISyntaxException e) {
            logger.error("Could not create URI '{}' for key '{}'!", uriStr, key, e);
        }
        return null;
    }

    /**
     * Loads a JavaFX {@code Image} from the image path specified by the key in the image bundle file.
     *
     * @param key the key as specified in the image bundle file
     * @return the JavaFX image
     * @throws MissingResourceException if no resource for the specified key can be found
     */
    public static Image getFxImage(String key) throws MissingResourceException {
        final String imagePath = ResourceBundle.getBundle(IMAGE_BUNDLE, Languages.getCurrentLocale()).getString(key);
        return new Image(BundleUtils.class.getResource(imagePath).toExternalForm());
    }

    public static FXMLLoader getFXMLLoader(String key) {
        return new FXMLLoader(getFXMLUrl(key), ResourceBundle.getBundle("org.terasology.launcher.bundle.LabelsBundle", Languages.getCurrentLocale()));
    }

    public static URL getFXMLUrl(String key) {
        final String url = ResourceBundle.getBundle(FXML_BUNDLE, Languages.getCurrentLocale()).getString(key);
        return BundleUtils.class.getResource(url);
    }

    public static URL getFXMLUrl(String key, String relative) {
        final String url = ResourceBundle.getBundle(FXML_BUNDLE, Languages.getCurrentLocale()).getString(key);
        return BundleUtils.class.getResource(url + '/' + relative);
    }

    public static String getStylesheet(String key) {
        return ResourceBundle.getBundle(FXML_BUNDLE, Languages.getCurrentLocale()).getString(key);
    }
}
