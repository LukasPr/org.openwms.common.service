/*
 * Copyright 2018 Heiko Scherrer
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.common.location;

import org.dozer.DozerConverter;

/**
 * A LocationConverter.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
public class LocationPKConverter extends DozerConverter<String, LocationPK> {

    /**
     * {@inheritDoc}
     */
    public LocationPKConverter() {
        super(String.class, LocationPK.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LocationPK convertTo(String source, LocationPK destination) {
        return LocationPK.fromString(source);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String convertFrom(LocationPK source, String destination) {
        return source != null ? source.toString() : null;
    }
}
