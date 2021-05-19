UPDATE system_cpu SET active = 'N' WHERE cpu_type = 'PRODUCTION';
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES1','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES2','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES3','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES4','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES5','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (5,'DRRES6','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (7,'DRAIR1','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (7,'DRAIR2','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (7,'DRAIR3','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (6,'DROSS1','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
INSERT INTO system_cpu(system_id, cpu_name, cpu_type, created_by, created_dt, active) VALUES (6,'DROSS2','PRODUCTION','MTPService','2017/01/01 10:00:00', 'Y');
UPDATE system AS v SET default_prod_cpu = s.id FROM system_cpu AS s WHERE v.id = 5 and s.cpu_name = 'DRRES1' and s.active = 'Y'
UPDATE system AS v SET default_prod_cpu = s.id FROM system_cpu AS s WHERE v.id = 6 and s.cpu_name = 'DROSS1' and s.active = 'Y'
UPDATE system AS v SET default_prod_cpu = s.id FROM system_cpu AS s WHERE v.id = 7 and s.cpu_name = 'DRAIR1' and s.active = 'Y'