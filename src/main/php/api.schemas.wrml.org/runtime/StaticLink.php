<?php
/*
 * Copyright (C) 2011 WRML.org <mark@wrml.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

require_once(DOCROOT . '/runtime/StaticSchema.php');

class StaticLink extends Model {
    
    public $rel;    
    public $href;
    public $requestTypes;    
    public $responseTypes;    
    public $stateExpression;
        
    function __construct($rel, $href) {
        $this->rel = $rel;        
        $this->href = $href;        
        $this->requestTypes = array();
        $this->responseTypes = array();
        $this->stateExpression = "true";
    }

    public function getRel() {
        return $this->rel;
    }
    
    public function &getRequestTypes() {
        return $this->requestTypes;
    }

    public function &getResponseTypes() {
        return $this->responseTypes;
    }
    
    public function getHref() {
        return $this->href;
    }
    
    public function setHref($href) {
        $this->href = $href;
    }

    public function getStateExpression() {
        return $this->stateExpression;
    }
    
    public function setStateExpression($stateExpression) {
        $this->stateExpression = $stateExpression;
    }
}
