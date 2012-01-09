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
require_once(DOCROOT . '/org/wrml/model/schema/Type.php');

class StaticField extends Model {
    
    public $type;    
    public $constraints;
    public $defaultValue;    
    public $description;    
    public $hidden;
    public $name;
    public $readOnly;
    public $required;
    public $title;    
    public $transient;
            
    public function __construct($name, $type = Type::TEXT_TYPE) {        
        $this->name = $name;
        $this->type = $type;
        $this->title = ucfirst($name);
        
        $this->constraints = array();

        $this->setHidden(false);
        $this->setReadOnly(false);
        $this->setRequired(false);
        $this->setTransient(false);
        
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

    public function &getConstraints() {
        return $this->constraints;
    }
    
    public function setDefaultValue($defaultValue) {
        $this->defaultValue = $defaultValue;
    }
    
    public function getDefaultValue() {
        return $this->defaultValue;
    }
    
    public function setReadOnly($readOnly) {
        $this->readOnly = $readOnly;
    }
    
    public function getReadOnly() {
        return $this->readOnly;
    }    

    public function setHidden($hidden) {
        $this->hidden = $hidden;
    }
    
    public function getHidden() {
        return $this->hidden;
    }

    public function setRequired($required) {
        $this->required = $required;
    }
    
    public function getRequired() {
        return $this->required;
    }

    public function setTitle($title) {
        $this->title = $title;
    }
    
    public function getTitle() {
        return $this->title;
    }

    public function setTransient($transient) {
        $this->transient = $transient;
    }
    
    public function getTransient() {
        return $this->transient;
    }
}
