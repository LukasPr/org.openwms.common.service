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

import org.ameba.exception.NotFoundException;
import org.ameba.mapping.BeanMapper;
import org.openwms.common.CommonConstants;
import org.openwms.common.location.api.LocationVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

/**
 * A LocationController.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@RestController
class LocationController {

    private final LocationService locationService;
    private final BeanMapper mapper;

    LocationController(LocationService locationService, BeanMapper mapper) {
        this.locationService = locationService;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = CommonConstants.API_LOCATIONS, params = {"locationPK"})
    Optional<LocationVO> findLocation(@RequestParam("locationPK") String locationPk) {
        if (!LocationPK.isValid(locationPk)) {
            throw new NotFoundException(format("Invalid location [%s]", locationPk));
        }
        Location location = locationService.findByLocationId(LocationPK.fromString(locationPk)).orElseThrow(() -> new NotFoundException(format("No Location with locationPk [%s] found", locationPk)));
        return Optional.ofNullable(mapper.map(location, LocationVO.class));
    }

    /**
     * {@inheritDoc}
     */
    @GetMapping(value = CommonConstants.API_LOCATIONS, params = {"plcCode"})
    Optional<LocationVO> findLocationByPlcCode(@RequestParam("plcCode") String plcCode) {
        Location location = locationService.findByPlcCode(plcCode).orElseThrow(() -> new NotFoundException(format("No Location with PLC Code [%s] found", plcCode)));
        return Optional.ofNullable(mapper.map(location, LocationVO.class));
    }


    @GetMapping(value = CommonConstants.API_LOCATIONS, params = {"locationGroupNames"})
    List<LocationVO> findLocationsForLocationGroups(@RequestParam("locationGroupNames") List<String> locationGroupNames) {
        List<Location> locations = locationService.findAllOf(locationGroupNames);
        return mapper.map(locations, LocationVO.class);
    }
}
