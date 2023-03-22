package host.luke.common.utils;

public class TypeUtil {

    public static Integer getTypeId(String typeName){
        if(typeName.equals("traffic")) return 1;
        if(typeName.equals("office")) return 2;
        if(typeName.equals("daily")) return 3;
        if(typeName.equals("service")) return 4;
        if(typeName.equals("digital_appliance")) return 5;
        if(typeName.equals("rent_decoration")) return 6;
        if(typeName.equals("communication")) return 7;
        if(typeName.equals("lodging")) return 8;
        if(typeName.equals("post")) return 9;
        if(typeName.equals("medical_treatment")) return 10;
        if(typeName.equals("repast")) return 11;
        if(typeName.equals("foodstuff")) return 12;
        if(typeName.equals("raiment")) return 13;
        if(typeName.equals("vehicle")) return 14;
        if(typeName.equals("education")) return 15;
        return 16;
    }


}
