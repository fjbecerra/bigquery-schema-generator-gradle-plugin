package com.pakius;

import java.util.List;

class BQSchemaClass1{
    private String anyfield1;
}

class BQSchemaClass2{
    private BQSchemaClass1 pc;
}

class BQSchemaClass3{
    private byte[] bytes;
}

class BQSchemaClass4{
    private List<String> lst;
    private List<BQSchemaClass1> lst2;
}

class BQSchemaClass5{
    private String field1;
}

class BQSchemaClass6 extends BQSchemaClass5 {
    private String field2;
}

