/*
 * Copyright 2024 OmniOne.
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

package org.omnione.did.sdk.issuer.model.data;

import org.omnione.did.sdk.issuer.model.enums.LogoImageType;

/**
 * Represents the logo image data structure.
 */
public class LogoImage {
    private LogoImageType format;
    private String link;
    private String value;

    public LogoImage() {
    }

    public LogoImage(LogoImageType format, String link, String value) {
        this.format = format;
        this.link = link;
        this.value = value;
    }

    public LogoImageType getFormat() {
        return format;
    }

    public void setFormat(LogoImageType format) {
        this.format = format;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "LogoImage{" +
                "format=" + format +
                ", link='" + link + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LogoImageType format;
        private String link;
        private String value;

        public Builder format(LogoImageType format) {
            this.format = format;
            return this;
        }

        public Builder link(String link) {
            this.link = link;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public LogoImage build() {
            return new LogoImage(format, link, value);
        }
    }
}
