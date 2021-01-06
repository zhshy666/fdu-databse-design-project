package project.backend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import project.backend.Controller.Request.DeleteOrAddHospitalNurseRequest;
import project.backend.Controller.Request.GetBedInfoRequest;
import project.backend.Entity.Bed;
import project.backend.Entity.BedInfo;
import project.backend.Entity.Patient;
import project.backend.Entity.TreatmentRegion;
import project.backend.Service.BedService;
import project.backend.Service.HospitalNurseService;
import project.backend.Service.PatientService;
import project.backend.Service.TreatmentRegionService;
import project.backend.Utils.Config;

import java.util.LinkedList;
import java.util.List;

@Controller
public class ChiefNurseController {
    private HospitalNurseService hospitalNurseService;
    private TreatmentRegionService treatmentRegionService;
    private PatientService patientService;
    private BedService bedService;

    @Autowired
    public ChiefNurseController(HospitalNurseService hospitalNurseService, TreatmentRegionService treatmentRegionService,
                                PatientService patientService, BedService bedService) {
        this.hospitalNurseService = hospitalNurseService;
        this.treatmentRegionService = treatmentRegionService;
        this.patientService = patientService;
        this.bedService = bedService;
    }

    @PostMapping("/deleteHospitalNurse")
    public ResponseEntity<?> deleteHospitalNurse(@RequestBody DeleteOrAddHospitalNurseRequest request){
        if (!request.getChief_nurse_id().startsWith("C")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String nurseId = request.getNurse_id();
        hospitalNurseService.deleteHospitalNurse(Config.CHIEF_NURSE, nurseId);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/addHospitalNurse")
    public ResponseEntity<?> addHospitalNurse(@RequestBody DeleteOrAddHospitalNurseRequest request){
        if (!request.getChief_nurse_id().startsWith("C")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String chiefNurseId = request.getChief_nurse_id();
        String nurseId = request.getNurse_id();

        TreatmentRegion region = treatmentRegionService.getTreatmentRegionByChiefNurseId(Config.CHIEF_NURSE, chiefNurseId);
        boolean flag = hospitalNurseService.addHospitalNurse(Config.CHIEF_NURSE, nurseId, region.getLevel());

        // 是否有空的床位
        if (flag) {
            boolean canTransfer = bedService.hasEmptyBed(Config.CHIEF_NURSE, region.getLevel());
            if (canTransfer) transferToCurrentRegion(region);
        }
        else {
            return new ResponseEntity<>("Info error", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok("success");
    }

    @PostMapping("/getBedInfo")
    public ResponseEntity<?> getBedInfo(@RequestBody GetBedInfoRequest request){
        if (!request.getChief_nurse_id().startsWith("C")){
            return new ResponseEntity<>("Not allowed", HttpStatus.FORBIDDEN);
        }
        String chiefNurseId = request.getChief_nurse_id();
        List<BedInfo> result = new LinkedList<>();
        TreatmentRegion region = treatmentRegionService.getTreatmentRegionByChiefNurseId(Config.CHIEF_NURSE, chiefNurseId);

        // 获取该区域的所有病床
        List<Bed> beds = bedService.getBedsByRegionLevel(Config.CHIEF_NURSE, region.getLevel());
        for (Bed bed: beds){
            BedInfo bedInfo;
            if (bed.getPatient_id() != null){
                // 对应的病人
                Patient patient = patientService.getPatientById(Config.CHIEF_NURSE, bed.getPatient_id());
                bedInfo = new BedInfo(patient.getPatient_id(), patient.getName(), patient.getGender(),
                        patient.getAge(), patient.getDisease_level(), patient.getLife_status(), patient.getNurse_id(),
                        patient.getTreatment_region_level());
            }
            else {
                bedInfo = new BedInfo();
                bedInfo.setPatient_id(null);
            }
            bedInfo.setBed_id(bed.getBed_id());
            result.add(bedInfo);
        }
        return ResponseEntity.ok(result);
    }

    // 获取待转入该区域的病人并进行转入
    private void transferToCurrentRegion(TreatmentRegion region){
        Patient patientNeedTransfer;
        // 1 查是否有病人等待转入该治疗区域，处于隔离区的病人优先
        List<Patient> patientsWaitToTransfer = patientService.getPatientsByDiseaseLevel(Config.ROOT, region.getLevel());
        if (patientsWaitToTransfer.isEmpty()){
            return;
        }
        patientNeedTransfer = patientsWaitToTransfer.get(0);
        for (Patient p : patientsWaitToTransfer){
            if (p.getTreatment_region_level() != null && p.getTreatment_region_level().equals("quarantine")){
                patientNeedTransfer = p;
                break;
            }
        }
        // 2 转移
        // ToDo: 发站内信
        patientService.appointHospitalNurse(Config.ROOT, patientNeedTransfer, region);
        patientService.appointBed(Config.ROOT, patientNeedTransfer, region);
    }
}
