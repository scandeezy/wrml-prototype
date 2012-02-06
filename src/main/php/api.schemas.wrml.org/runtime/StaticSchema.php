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

require_once(DOCROOT . '/runtime/Model.php');

class StaticSchema extends Model {
    
    private $context;
    
    public $id;
    public $name;
    public $version;
    public $description;
    public $baseSchemaIds;
    public $constraints;
    public $fields;
    public $links;
    
    public function __construct($context) {
        $this->context = $context;
        $this->version = 1;
        $this->baseSchemaIds = array();
        $this->constraints = array();
        $this->fields = array();
        $this->links = array();                
    }

    public function getContext() {
        return $this->context;
    }
    
    public function getId() {
        return $this->id;
    }
    
    
    public function setId($id) {       
        $this->id = $id;        
        $this->name = basename($id);

        $uri = parse_url($id);
        $uriPath = $uri['path'];
        $uriPathSegments = explode('/', $uriPath);
        $uriPathSegmentsCount = count($uriPathSegments);
        unset($uriPathSegments[$uriPathSegmentsCount - 1]);
        $parentUriPath = implode('/', $uriPathSegments);

        $port = $uri['port'];
        $port = ($port != null) ? $port : "";
        $parentUri = $uri['scheme'] . '://' . $uri['host'] . $port . $parentUriPath;
    }       
        
    
    public function getParentId() {
        return $this->parentId;
    }

    public function getName() {
        return $this->name;
    }

    public function setDescription($description) {
        $this->description = $description;
    }
    
    public function getDescription() {
        return $this->description;
    }
    
    public function setVersion($version) {
        $this->version = $version;
    }
    
    public function getVersion() {
        return $this->version;
    }

    public function &getBaseSchemaIds() {
        return $this->baseSchemaIds;
    }
    
    public function &getConstraints() {
        return $this->constraints;
    }
    
    public function &getFields() {
        return $this->fields;
    }
    
    public function &getLinks() {
        return $this->links;
    }
}
