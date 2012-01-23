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

class Context {
    
    public function getSchemaId() {
        $path = $this->getRequestPath();
        $pathElements = explode('/', $path);
        $schemaName = end($pathElements);
        $schemaPath = str_replace('.', '/', $schemaName);
        return 'http://api.schemas.wrml.org/' . $schemaPath;
    }

    public function getRequestFullUri() {   
        $requestPath = $this->getRequestPath();
        return $this->getRequestUri($requestPath);
    }
    
    public function getRequestPath() {
        if(isset($_SERVER['REQUEST_URI'])){
            $path = $_SERVER['REQUEST_URI'];            
        }
        else{
            $path = $_SERVER['PHP_SELF'];
        }
        
        return $path;
    }
            
    public function getRequestUri($path) {        
        $s = empty($_SERVER["HTTPS"]) ? '' : ($_SERVER["HTTPS"] == "on") ? "s" : "";
        $protocol = $this->strleft(strtolower($_SERVER["SERVER_PROTOCOL"]), "/") . $s;
        $port = ($_SERVER["SERVER_PORT"] == "80") ? "" : (":" . $_SERVER["SERVER_PORT"]);
        return $protocol . "://" . $_SERVER['SERVER_NAME'] . $port . $path;   
    }
    
    
    public function setHeaders() {    

        // TODO: Check the Accept header before setting the Content-Type
        
        $schemaId = $this->getSchemaId();        
        if ($schemaId != null) {                        
            header("Content-type: application/wrml; schema=\"" . $schemaId . "\"");
        }
        else {            
            header('Content-type: application/json');
        }
                
    }
    
    /**
     * Example usage:
     *   
     *    $mime = getBestSupportedMimeType(Array ('application/xhtml+xml', 'text/html')); 
     */
    function getBestSupportedMimeType($mimeTypes = null) { 
        // Values will be stored in this array 
        $AcceptTypes = Array (); 
     
        // Accept header is case insensitive, and whitespace isn�t important 
        $accept = strtolower(str_replace(' ', '', $_SERVER['HTTP_ACCEPT'])); 
        // divide it into parts in the place of a "," 
        $accept = explode(',', $accept); 
        foreach ($accept as $a) { 
            // the default quality is 1. 
            $q = 1; 
            // check if there is a different quality 
            if (strpos($a, ';q=')) { 
                // divide "mime/type;q=X" into two parts: "mime/type" i "X" 
                list($a, $q) = explode(';q=', $a); 
            } 
            // mime-type $a is accepted with the quality $q 
            // WARNING: $q == 0 means, that mime-type isn�t supported! 
            $AcceptTypes[$a] = $q; 
        } 
        arsort($AcceptTypes); 
     
        // if no parameter was passed, just return parsed data 
        if (!$mimeTypes) return $AcceptTypes; 
     
        $mimeTypes = array_map('strtolower', (array)$mimeTypes); 
     
        // let�s check our supported types: 
        foreach ($AcceptTypes as $mime => $q) { 
           if ($q && in_array($mime, $mimeTypes)) return $mime; 
        } 
        // no mime-type found 
        return null; 
    } 

    
    
    
    public function createResponseEntity() {
               
        $requestBody = file_get_contents('php://input');        
        $requestPath = $this->getRequestPath();
        $requestUri = $this->getRequestUri($requestPath);
        $requestFilePath = DOCROOT . $requestPath;        
                
        if (is_dir($requestFilePath)) {
            // TODO: Handle collections
            $responseEntity = "TODO: " . $requestUri . " is a Collection.";
        }
        else {
            
            $requestScriptPath = $requestFilePath . Context::PHP_FILE_EXTENSION;
            
            if (file_exists($requestScriptPath)) {
                include_once($requestScriptPath);
                $className = basename($requestPath);
                $responseEntity = new $className($this, $requestUri);
            }
            else {
                $schema = new StaticSchema($this, $requestUri);
                $baseSchemaIds = &$schema->getBaseSchemaIds();
                $baseSchemaIds[] = $this->getRequestUri(Context::DOCUMENT_SCHEMA_PATH);            
                $schema->setDescription('An autogenerated Schema.');               
                $responseEntity = $schema;
            }
        }
        
        if ($responseEntity instanceof StaticSchema) {
            $responseEntity->setId($requestUri); 
        }
        
        return $responseEntity;
    }
        
    public function createLink($relPath, $href) {
        $relId = Context::RELATIONS_API_DOCROOT . $relPath;
        $link = new StaticLink($relId, $href);
        return $link;
    }
           
    public function createConstraint($constraintPath, $description) {
        $constraintId = Context::CONSTRAINT_API_DOCROOT . $constraintPath;
        $constraint = new StaticConstraint($constraintId);
        $constraint->setDescription($description);
        return $constraint;
    }
    
    public function createUriTextSyntaxConstraint() {
        return $this->createConstraint('/' . Type::TEXT_TYPE . '/Syntax?validator=' . urlencode(Context::VALIDATORS_API_DOCROOT . '/org/wrml/util/validators/UriSyntaxValidator'), 'Constrains the text to conform to the syntax of a URI (see http://www.ietf.org/rfc/rfc3986.txt)');
    }

    public function createSchemaConstraint($schemId) {
        return $this->createConstraint('/' . Type::MODEL_TYPE . '?schema=\"' . urlencode($schemId) . '\"', 'Constrains the model to conform to a specified schema.');
    }
    
    public function createChoiceMenuConstraint($choiceMenuId) {
        return $this->createConstraint('/' . Type::CHOICE_TYPE . '?menu=\"' . urlencode($choiceMenuId) . '\"', 'Constrains the choice to one of the menu\'s selections.');
    }
    
    
    function strleft($s1, $s2) {
        return substr($s1, 0, strpos($s1, $s2));
    }

}
