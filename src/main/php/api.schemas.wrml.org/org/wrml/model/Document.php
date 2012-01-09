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
require_once(DOCROOT . '/runtime/StaticField.php');
require_once(DOCROOT . '/org/wrml/model/schema/Type.php');

class Document extends StaticSchema {

    function __construct($context) {
        parent::__construct($context);
        
        $this->setDescription("A resource archetype used to model a singular concept.");
                
        $context = $this->getContext();
                
        $fields = &$this->getFields();
        
        // id
        $idField = new StaticField("id", Type::TEXT_TYPE);
        $idField->setDescription("The document's identifier.");        
        
        $uriTextSyntaxConstraint = $context->createUriTextSyntaxConstraint()->getId();
        
        $idFieldConstraints = &$idField->getConstraints();        
        $idFieldConstraints[] = $uriTextSyntaxConstraint;
        
        $fields[] = $idField;

        // parentId
        $parentField = new StaticField("parentId", Type::TEXT_TYPE);
        $parentField->setDescription("The document's parent identifier.");        
        
        $parentFieldConstraints = &$parentField->getConstraints();        
        $parentFieldConstraints[] = $uriTextSyntaxConstraint;
        
        $fields[] = $parentField;

        $links = &$this->getLinks();

        // parentId
        $self = $context->createLink("/common/self", $this->getId());                        
        $links[] = $self;        
    }

}
