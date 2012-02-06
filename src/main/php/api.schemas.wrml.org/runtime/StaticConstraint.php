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

class StaticConstraint extends Model {
       
    public $id;
    public $name;
    public $version;
    public $description;
    public $title;  
            
    function __construct($id) {
        $this->id = $id;
        $this->name = basename($id);
        $this->title = ucfirst($this->name);
        
        $this->version = 1;
    }

    public function getContext() {
        return $this->context;
    }
    
    public function getId() {
        return $this->id;
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
    
    public function setTitle($title) {
        $this->title = $title;
    }
    
    public function getTitle() {
        return $this->title;
    }
}
