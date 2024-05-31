package com.sdk.ipassplussdk.model.response.transaction_details

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName


data class TransactionDetailResponse (

    @SerializedName("Apistatus"  ) var Apistatus  : Boolean? = null,
    @SerializedName("Apimessage" ) var Apimessage : String?  = null,
    @SerializedName("data"       ) var data       : JsonObject?    = null

)

data class Reason (

    @SerializedName("Status" ) var Status : String? = null,
    @SerializedName("Text"   ) var Text   : String? = null

)



data class MRZ (

    @SerializedName("Age" )                            var Age :                        String?    =       null,
    @SerializedName("Surname And Given Names" )        var Surname_And_Given_Names :    String? = null,
    @SerializedName("Date of Birth" )                  var Date_of_Birth      :         String?    = null,
    @SerializedName("Document Number" )                var Document_Number     :        String?    =      null,
    @SerializedName("Document Number Checkdigit" )     var Document_Number_Checkdigit : String?    = null,
    @SerializedName("Issuing State Code" )             var Issuing_State_Code       :   String?    = null,
    @SerializedName("Date of Birth Checkdigit" )       var Date_of_Birth_Checkdigit :   String? = null,
    @SerializedName("Sex" )                            var Sex :                        String?    =       null,
    @SerializedName("Date of Expiry"     )             var Date_of_Expiry :             String?    = null,
    @SerializedName("Date of Expiry Checkdigit"   )    var Date_of_Expiry_Checkdigit :  String? = null,
    @SerializedName("Nationality Code"       )         var Nationality_Code :           String?    =      null,
    @SerializedName("Final Checkdigit" )               var Final_Checkdigit :           String?    =      null,
    @SerializedName("Document Class Code"       )      var Document_Class_Code       :  String?    = null,
    @SerializedName("RemainderTerm")                   var RemainderTerm :              String?    =       null,
    @SerializedName("MRZ Strings" )                    var MRZ_Strings :                String?    =      null,
    @SerializedName("Nationality" )                    var Nationality :                String?    =       null,
    @SerializedName("Issuing State Name"       )       var Issuing_State_Name :         String?    = null,
    @SerializedName("Optional Data" )                  var Optional_Data :              String?    =      null,
    @SerializedName("Surname" )                        var Surname :                    String?    =       null,
    @SerializedName("Given Names" )                    var Given_Names      :           String?    =      null,
    @SerializedName("MRZ Type" )                       var MRZ_Type       :             String?    =      null

)


data class Visual (

    @SerializedName("Surname And Given Names"  )            var Surname_And_Given_Names   : String? = null,
    @SerializedName("Date of Birth"        )                var Date_of_Birth        :      String? = null,
    @SerializedName("Document Number" )                     var Document_Number:            String?      =     null,
    @SerializedName("Sex" )                                 var Sex:                        String? =       null,
    @SerializedName("Date of Expiry"       )                var Date_of_Expiry       :      String? = null,
    @SerializedName("Place of Birth"        )               var Place_of_Birth        :     String? = null,
    @SerializedName("Mothers Name"   )                      var Mothers_Name    :           String?      =     null,
    @SerializedName("Blood Group"  )                        var Blood_Group   :             String?      =     null,
    @SerializedName("Place of Registration" )               var Place_of_Registration :     String? = null,
    @SerializedName("Place of Issue"        )               var Place_of_Issue        :     String? = null,
    @SerializedName("Address" )                             var Address :                   String? =       null

)

data class MRZVIZComparison (

    @SerializedName("Age" )                                   var Age           :            String? =       null,
    @SerializedName("Surname And Given Names"        )        var Surname_And_Given_Names   :String? = null,
    @SerializedName("Date of Birth"  )                        var Date_of_Birth   :          String? = null,
    @SerializedName("Document Number" )                       var Document_Number  :          String? =     null,
    @SerializedName("Issuing State   Code"   )                var Issuing_State_Code    :     String? = null,
    @SerializedName("Sex" )                                   var Sex           :              String? =       null,
    @SerializedName("Date of Expiry" )                        var Date_of_Expiry  :           String? = null,
    @SerializedName("RemainderTerm" )                         var RemainderTerm :              String? =       null,
    @SerializedName("Issuing State Name"   )                  var Issuing_State_Name    :     String? = null

)



data class DocDetails (

    @SerializedName("MRZ"                ) var MRZ              : MRZ?              = MRZ(),
    @SerializedName("Visual"             ) var Visual           : Visual?           = Visual(),
    @SerializedName("MRZ_VIZ_Comparison" ) var MRZVIZComparison : MRZVIZComparison? = MRZVIZComparison()

)

data class Front (

    @SerializedName("Security alert"        )   var     Security_alert             : Boolean?       = null,
    @SerializedName("Photo    Copy"         )   var     Photo_Copy              : Boolean?       = null,
    @SerializedName("Image    Manupulation" )   var     Image_Manupulation      : Boolean?       = null,
    @SerializedName("Warning" )             var Warning :        ArrayList<String> = arrayListOf(),
    @SerializedName("Map"     )             var Map     :        String?           = null

)


data class Back (

    @SerializedName("Security alert"        )   var     Security_alert             : Boolean?       = null,
    @SerializedName("Photo    Copy"         )   var     Photo_Copy              : Boolean?       = null,
    @SerializedName("Image    Manupulation" )   var     Image_Manupulation      : Boolean?       = null,
    @SerializedName("Warning" )             var Warning : ArrayList<String> = arrayListOf(),
    @SerializedName("Map"     )             var Map     :  String?           = null

)

data class DocumentAuthentication (

    @SerializedName("Front" ) var Front : Front? = Front(),
    @SerializedName("Back"  ) var Back  : Back?  = Back()

)

data class DocImages (

    @SerializedName("Portrait"             )         var Portrait             :     String?  = null,
    @SerializedName("Ghost                 portrait" )   var Ghost_portrait : String? = null,
    @SerializedName("Signature"            )         var Signature            :     String?  = null,
    @SerializedName("documentFrontSide"    )         var documentFrontSide    :     String?  = null,
    @SerializedName("documentBackSide"     )         var documentBackSide     :     String?  = null,
    @SerializedName("documentFrontSideRaw" )         var documentFrontSideRaw :     String?  = null,
    @SerializedName("documentBackSideRaw"  )         var documentBackSideRaw  :     String?  = null

)

data class LivenessResult (

    @SerializedName("AuditImages" ) var AuditImages : ArrayList<String> = arrayListOf(),
    @SerializedName("Confidence"  ) var Confidence  : Double?           = null,
    @SerializedName("SessionId"   ) var SessionId   : String?           = null,
    @SerializedName("Status"      ) var Status      : String?           = null,
    @SerializedName("faceImage"   ) var faceImage   : String?           = null

)

data class FaceMatchngResult (

    @SerializedName("_id"               ) var Id                : String? = null,
    @SerializedName("sourceImageBase64" ) var sourceImageBase64 : String? = null,
    @SerializedName("targetImageBase64" ) var targetImageBase64 : String? = null,
    @SerializedName("facePercentage"    ) var facePercentage    : Int?    = null,
    @SerializedName("timestamp"         ) var timestamp         : String? = null,
    @SerializedName("email"             ) var email             : String? = null,
    @SerializedName("sid"               ) var sid               : String? = null,
    @SerializedName("status"            ) var status            : String? = null,
    @SerializedName("confidence"        ) var confidence        : String? = null,
    @SerializedName("__v"               ) var _v                : Int?    = null

)
data class Data (

    @SerializedName("OverAllStatus"          ) var OverAllStatus          : String?                           = null,
    @SerializedName("Reason"                 ) var Reason                 : ArrayList<Reason>                 = arrayListOf(),
    @SerializedName("DocDetails"             ) var DocDetails             : DocDetails?                       = DocDetails(),
    @SerializedName("DocumentAuthentication" ) var DocumentAuthentication : ArrayList<DocumentAuthentication> = arrayListOf(),
    @SerializedName("DocImages"              ) var DocImages              : DocImages?                        = DocImages(),
    @SerializedName("livenessResult"         ) var livenessResult         : LivenessResult?                   = LivenessResult(),
    @SerializedName("faceMatchngResult"      ) var faceMatchngResult      : FaceMatchngResult?                = FaceMatchngResult()

)