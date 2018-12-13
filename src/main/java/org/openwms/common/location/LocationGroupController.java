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
import org.ameba.i18n.Translator;
import org.ameba.mapping.BeanMapper;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.openwms.common.CommonConstants;
import org.openwms.common.location.api.ErrorCodeTransformers;
import org.openwms.common.location.api.ErrorCodeVO;
import org.openwms.common.location.api.LocationGroupState;
import org.openwms.common.location.api.LocationGroupVO;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * A LocationGroupController.
 *
 * @author <a href="mailto:scherrer@openwms.org">Heiko Scherrer</a>
 */
@RestController(CommonConstants.API_LOCATION_GROUPS)
class LocationGroupController {

    private final LocationGroupService locationGroupService;
    private final Translator translator;
    private final BeanMapper mapper;
    private final ErrorCodeTransformers.GroupStateIn groupStateIn;
    private final ErrorCodeTransformers.GroupStateOut groupStateOut;

    LocationGroupController(LocationGroupService locationGroupService, Translator translator, BeanMapper mapper, ErrorCodeTransformers.GroupStateIn groupStateIn, ErrorCodeTransformers.GroupStateOut groupStateOut) {
        this.locationGroupService = locationGroupService;
        this.translator = translator;
        this.mapper = mapper;
        this.groupStateIn = groupStateIn;
        this.groupStateOut = groupStateOut;
    }

    @GetMapping(value = CommonConstants.API_LOCATION_GROUPS, params = {"name"})
    LocationGroupVO findByName(@RequestParam("name") String name) {
        Optional<LocationGroup> opt = locationGroupService.findByName(name);
        if (opt.isPresent()) {
            LocationGroup locationGroup = opt.get();
            LocationGroupVO result = mapper.map(locationGroup, LocationGroupVO.class);
            if (locationGroup.hasParent()) {
                result.add(linkTo(methodOn(LocationGroupController.class).findByName(locationGroup.getParent().getName())).withRel("_parent"));
            }
            return result;
        }
        throw new NotFoundException(format("LocationGroup with name [%s] does not exist", name));
    }

    @GetMapping(value = CommonConstants.API_LOCATION_GROUPS)
    List<LocationGroupVO> findAll() {
        List<LocationGroup> all = locationGroupService.findAll();
        return all == null ? Collections.emptyList() : mapper.map(all, LocationGroupVO.class);
    }

    @PutMapping(value = CommonConstants.API_LOCATION_GROUPS, params = {"name"})
    @PatchMapping(value = CommonConstants.API_LOCATION_GROUPS, params = {"name"})
    void updateState(@RequestParam(name = "name") String locationGroupName, @RequestBody ErrorCodeVO errorCode) {
        locationGroupService.changeGroupStates(locationGroupName, groupStateIn.transform(errorCode.getErrorCode()), groupStateOut.transform(errorCode.getErrorCode()));
    }


    @PatchMapping(value = CommonConstants.API_LOCATION_GROUPS + "/{id}")
    void save(@PathVariable String id, @RequestParam(name = "statein", required = false) LocationGroupState stateIn, @RequestParam(name = "stateout", required = false) LocationGroupState stateOut, HttpServletRequest req, HttpServletResponse res) {
        locationGroupService.changeGroupState(id, stateIn, stateOut);
        res.addHeader(HttpHeaders.LOCATION, getLocationForCreatedResource(req, id));
    }

    public String getLocationForCreatedResource(HttpServletRequest req, String objId) {
        StringBuffer url = req.getRequestURL();
        UriTemplate template = new UriTemplate(url.append("/{objId}/").toString());
        return template.expand(objId).toASCIIString();
    }
}
