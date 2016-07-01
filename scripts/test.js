// 使用方法：mongo < processHospitalDepartmentId.js
use cxszsh

var hpCollection = db.getCollection("hospital");

hpCollection.find().forEach(function(doc) {
    if (doc.departments == null){
        return;
    }

    for (var i = 0; i < doc.departments.length; i++) {
        if (doc.departments[i]._id == null){
            doc.departments[i]._id = ObjectId();    
        }else {
            print("已有department._id，跳过:" + doc.departments[i].name);
        }
    }
    hpCollection.update({ "_id" : doc._id }, doc);
})
