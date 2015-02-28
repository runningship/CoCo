
function loadHardwareInfo(callback){
    try{
    var exec = require('child_process').execFile;
    var command = "hinf.exe";
    var xx = exec(command, function(err, stdout, stderr) {
        var fs=require("fs");
        var result = fs.readFileSync("sys.data","utf-8");
        result= result.trim();
        result = result.replace(/\r/g,"");
        var lines = result.split("\n");
        var json = JSON.parse("{}");
        for(var index=0;index<lines.length;index++){
            var line = lines[index];
            var pair = line.split("=");
            if(pair.length>1){
                json[pair[0]]=pair[1];
            }
        }
        fs.unlink('sys.data', function (err) {
          console.log('successfully deleted sys.data');
        });
        callback(json);
    });
    }catch(e){
        blockAlert(e);
    }
}