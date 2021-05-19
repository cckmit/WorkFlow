delete from vpars a using system s, platform p where a.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from system_cpu a using system s, platform p where a.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from load_window a using load_categories l, system s, platform p where a.load_category_id = l.id and l.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from load_freeze a using load_categories l, system s, platform p where a.load_category_id = l.id and l.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from load_categories a using system s, platform p where a.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from put_level a using system s, platform p where a.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
delete from pddds_library a using system s, platform p where a.system_id = s.id and s.platform_id = p.id and p.name like 'Delta';
update system s set active = 'N' from platform p where s.active = 'Y' and s.platform_id = p.id and p.name like 'Delta';
update platform set active = 'N' where active = 'Y' and name like 'Delta';
