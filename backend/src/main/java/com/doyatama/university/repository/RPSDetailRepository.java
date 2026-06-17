package com.doyatama.university.repository;

import com.doyatama.university.helper.HBaseCustomClient;
import com.doyatama.university.model.*;
import com.google.gson.Gson;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RPSDetailRepository {
    Configuration conf = HBaseConfiguration.create();
    String tableName = "rps_details";

    public List<RPSDetail> findAll(int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("week", "week");
        columnMapping.put("rps", "rps");
        columnMapping.put("sub_cp_mk", "sub_cp_mk");
        columnMapping.put("weekLabel", "weekLabel");
        columnMapping.put("learning_materials", "learning_materials");
        columnMapping.put("form_learning", "form_learning");
        columnMapping.put("learning_methods", "learning_methods");
        columnMapping.put("assignments", "assignments");
        columnMapping.put("estimated_times", "estimated_times");
        columnMapping.put("student_learning_experiences", "student_learning_experiences");
        columnMapping.put("assessment_criterias", "assessment_criterias");
        columnMapping.put("appraisal_forms", "appraisal_forms");
        columnMapping.put("assessment_indicators", "assessment_indicators");
        columnMapping.put("weight", "weight");
        columnMapping.put("created_at", "created_at");
        return client.showListTable(tableUsers.toString(), columnMapping, RPSDetail.class, size);
    }

    public List<RPSDetail> findByRpsID(String rpsID, int size) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("week", "week");
        columnMapping.put("rps", "rps");
        columnMapping.put("sub_cp_mk", "sub_cp_mk");
        columnMapping.put("weekLabel", "weekLabel");
        columnMapping.put("learning_materials", "learning_materials");
        columnMapping.put("form_learning", "form_learning");
        columnMapping.put("learning_methods", "learning_methods");
        columnMapping.put("assignments", "assignments");
        columnMapping.put("estimated_times", "estimated_times");
        columnMapping.put("student_learning_experiences", "student_learning_experiences");
        columnMapping.put("assessment_criterias", "assessment_criterias");
        columnMapping.put("appraisal_forms", "appraisal_forms");
        columnMapping.put("assessment_indicators", "assessment_indicators");
        columnMapping.put("weight", "weight");
        columnMapping.put("created_at", "created_at");

        return client.getDataListByColumn(tableUsers.toString(), columnMapping, "rps", "id", rpsID, RPSDetail.class,
                size);
    }

    public RPSDetail save(RPSDetail rpsDetail) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        ObjectMapper mapper = new ObjectMapper(); // Define ObjectMapper instance here
        String week = rpsDetail.getWeek().toString();
        // String rowKey = UUID.randomUUID().toString().substring(0, 20 - week.length())
        // + "-" + week;
        String rowKey;
        if (rpsDetail.getId() != null && !rpsDetail.getId().isEmpty()) {
            rowKey = rpsDetail.getId() + "-" + week;
        } else {
            rowKey = UUID.randomUUID().toString().substring(0, 10) + "-" + week;
        }

        TableName tableRPSDetail = TableName.valueOf(tableName);
        client.insertRecord(tableRPSDetail, rowKey, "main", "id", rowKey);
        client.insertRecord(tableRPSDetail, rowKey, "main", "week", rpsDetail.getWeek().toString());
        client.insertRecord(tableRPSDetail, rowKey, "main", "weekLabel", rpsDetail.getWeekLabel());

        client.insertRecord(tableRPSDetail, rowKey, "rps", "id", rpsDetail.getRps().getId());
        client.insertRecord(tableRPSDetail, rowKey, "rps", "name", rpsDetail.getRps().getName());
        client.insertRecord(tableRPSDetail, rowKey, "main", "sub_cp_mk", rpsDetail.getSub_cp_mk().toString());

        // learning_material
        for (int i = 0; i < rpsDetail.getLearning_materials().size(); i++) {
            String learningMaterial = rpsDetail.getLearning_materials().get(i);
            client.insertRecord(tableRPSDetail, rowKey, "learning_materials", "lm_" + i, learningMaterial);
        }

        // form_learning (make nullable)
        if (rpsDetail.getForm_learning() != null) {
            client.insertRecord(tableRPSDetail, rowKey, "form_learning", "id", rpsDetail.getForm_learning().getId());
            client.insertRecord(tableRPSDetail, rowKey, "form_learning", "name",
                    rpsDetail.getForm_learning().getName());
        }

        // learning_method
        for (int i = 0; i < rpsDetail.getLearning_methods().size(); i++) {
            LearningMethod learningMethod = rpsDetail.getLearning_methods().get(i);
            String learningMethodJson = mapper.writeValueAsString(learningMethod);
            client.insertRecord(tableRPSDetail, rowKey, "learning_methods", "lm_" + i, learningMethodJson);
        }
        // assignment
        for (int i = 0; i < rpsDetail.getAssignments().size(); i++) {
            String assignment = rpsDetail.getAssignments().get(i);
            client.insertRecord(tableRPSDetail, rowKey, "assignments", "lm_" + i, assignment);
        }

        // estimated_time
        for (int i = 0; i < rpsDetail.getEstimated_times().size(); i++) {
            String estimatedTime = rpsDetail.getEstimated_times().get(i);
            client.insertRecord(tableRPSDetail, rowKey, "estimated_times", "et_" + i, estimatedTime);
        }

        // student_learning_experience
        for (int i = 0; i < rpsDetail.getStudent_learning_experiences().size(); i++) {
            String studentLearningExperience = rpsDetail.getStudent_learning_experiences().get(i);
            client.insertRecord(tableRPSDetail, rowKey, "student_learning_experiences", "sle_" + i,
                    studentLearningExperience);
        }

        // assessment_criteria
        for (int i = 0; i < rpsDetail.getAssessment_criterias().size(); i++) {
            AssessmentCriteria assessmentCriteria = rpsDetail.getAssessment_criterias().get(i);
            String assessmentCriteriaJson = mapper.writeValueAsString(assessmentCriteria);
            client.insertRecord(tableRPSDetail, rowKey, "assessment_criterias", "ac_" + i, assessmentCriteriaJson);
        }

        // appraisal_form
        for (int i = 0; i < rpsDetail.getAppraisal_forms().size(); i++) {
            AppraisalForm appraisalForm = rpsDetail.getAppraisal_forms().get(i);
            String appraisalFormJson = mapper.writeValueAsString(appraisalForm);
            client.insertRecord(tableRPSDetail, rowKey, "appraisal_forms", "af_" + i, appraisalFormJson);
        }

        // assessment_indicator
        for (int i = 0; i < rpsDetail.getAssessment_indicators().size(); i++) {
            String assessmentIndicator = rpsDetail.getAssessment_indicators().get(i);
            client.insertRecord(tableRPSDetail, rowKey, "assessment_indicators", "ai_" + i, assessmentIndicator);
        }

        client.insertRecord(tableRPSDetail, rowKey, "main", "weight", rpsDetail.getWeight().toString());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableRPSDetail, rowKey, "detail", "created_by", "Doyatama");
        client.insertRecord(tableRPSDetail, rowKey, "detail", "created_at", instant.toString());
        return rpsDetail;
    }

    public RPSDetail findById(String rpsDetailId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableUsers = TableName.valueOf(tableName);
        Map<String, String> columnMapping = new HashMap<>();

        // Add the mappings to the HashMap
        columnMapping.put("id", "id");
        columnMapping.put("week", "week");
        columnMapping.put("rps", "rps");
        columnMapping.put("sub_cp_mk", "sub_cp_mk");
        columnMapping.put("learning_materials", "learning_materials");
        columnMapping.put("form_learning", "form_learning");
        columnMapping.put("learning_methods", "learning_methods");
        columnMapping.put("assignments", "assignments");
        columnMapping.put("estimated_times", "estimated_times");
        columnMapping.put("student_learning_experiences", "student_learning_experiences");
        columnMapping.put("assessment_criterias", "assessment_criterias");
        columnMapping.put("appraisal_forms", "appraisal_forms");
        columnMapping.put("assessment_indicators", "assessment_indicators");
        columnMapping.put("weight", "weight");
        columnMapping.put("created_at", "created_at");
        return client.showDataTable(tableUsers.toString(), columnMapping, rpsDetailId, RPSDetail.class);
    }

    public RPSDetail update(String rpsDetailId, RPSDetail rpsDetail) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);

        TableName tableRPSDetail = TableName.valueOf(tableName);
        client.insertRecord(tableRPSDetail, rpsDetailId, "main", "week", rpsDetail.getWeek().toString());
        client.insertRecord(tableRPSDetail, rpsDetailId, "rps", "id", rpsDetail.getRps().getId());
        client.insertRecord(tableRPSDetail, rpsDetailId, "rps", "name", rpsDetail.getRps().getName());
        client.insertRecord(tableRPSDetail, rpsDetailId, "main", "sub_cp_mk", rpsDetail.getSub_cp_mk().toString());
        // learning_material
        for (int i = 0; i < rpsDetail.getLearning_materials().size(); i++) {
            String learningMaterial = rpsDetail.getLearning_materials().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "learning_materials", "lm_" + i, learningMaterial);
        }

        client.insertRecord(tableRPSDetail, rpsDetailId, "form_learning", "id", rpsDetail.getForm_learning().getId());
        client.insertRecord(tableRPSDetail, rpsDetailId, "form_learning", "name",
                rpsDetail.getForm_learning().getName());

        // learning_method
        for (int i = 0; i < rpsDetail.getLearning_methods().size(); i++) {
            LearningMethod learningMethod = rpsDetail.getLearning_methods().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "learning_methods", "lm_" + i,
                    new Gson().toJson(learningMethod));
        }

        // assignment
        for (int i = 0; i < rpsDetail.getAssignments().size(); i++) {
            String assignment = rpsDetail.getAssignments().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "assignments", "lm_" + i, assignment);
        }

        // estimated_time
        for (int i = 0; i < rpsDetail.getEstimated_times().size(); i++) {
            String estimatedTime = rpsDetail.getEstimated_times().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "estimated_times", "et_" + i, estimatedTime);
        }

        // student_learning_experience
        for (int i = 0; i < rpsDetail.getStudent_learning_experiences().size(); i++) {
            String studentLearningExperience = rpsDetail.getStudent_learning_experiences().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "estimated_times", "et_" + i, studentLearningExperience);
        }

        // assessment_criteria
        for (int i = 0; i < rpsDetail.getAssessment_criterias().size(); i++) {
            AssessmentCriteria assessmentCriteria = rpsDetail.getAssessment_criterias().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "assessment_criterias", "ac_" + i,
                    new Gson().toJson(assessmentCriteria));
        }

        // appraisal_form
        for (int i = 0; i < rpsDetail.getAppraisal_forms().size(); i++) {
            AppraisalForm appraisalForm = rpsDetail.getAppraisal_forms().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "appraisal_forms", "af_" + i,
                    new Gson().toJson(appraisalForm));
        }

        // assessment_indicator
        for (int i = 0; i < rpsDetail.getAssessment_indicators().size(); i++) {
            String assessmentIndicator = rpsDetail.getAssessment_indicators().get(i);
            client.insertRecord(tableRPSDetail, rpsDetailId, "assessment_indicators", "ai_" + i, assessmentIndicator);
        }

        client.insertRecord(tableRPSDetail, rpsDetailId, "main", "weight", rpsDetail.getWeight().toString());

        // Get time now
        ZoneId zoneId = ZoneId.of("Asia/Jakarta");
        ZonedDateTime zonedDateTime = ZonedDateTime.now(zoneId);
        Instant instant = zonedDateTime.toInstant();

        client.insertRecord(tableRPSDetail, rpsDetailId, "detail", "created_by", "Doyatama");
        client.insertRecord(tableRPSDetail, rpsDetailId, "detail", "created_at", instant.toString());
        return rpsDetail;
    }

    public boolean deleteById(String rpsDetailId) throws IOException {
        HBaseCustomClient client = new HBaseCustomClient(conf);
        client.deleteRecord(tableName, rpsDetailId);
        return true;
    }
}
