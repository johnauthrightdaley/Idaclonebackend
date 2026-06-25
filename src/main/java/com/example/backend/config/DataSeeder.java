package com.example.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.backend.entity.Source;
import com.example.backend.entity.SourceAccount;
import com.example.backend.repository.SourceAccountRepository;
import com.example.backend.repository.SourceRepository;

/**
 * Seeds dummy Connections data on startup so the Admin -> Connections screens
 * have something to render. Idempotent: skips seeding if any source already
 * exists, so it's safe across restarts with {@code ddl-auto=update}.
 *
 * Produces exactly 16 sources ("16 Results") and 13 accounts on the
 * "XYZ AD_0171" Active Directory source ("13 Accounts"), matching the reference.
 */
@Component
public class DataSeeder implements CommandLineRunner {

	private final SourceRepository sourceRepository;
	private final SourceAccountRepository accountRepository;

	public DataSeeder(SourceRepository sourceRepository, SourceAccountRepository accountRepository) {
		this.sourceRepository = sourceRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public void run(String... args) {
		if (sourceRepository.count() > 0) {
			return;
		}

		// The source that drives the App Page header ("XYZ AD_0171: Active Directory").
		Source ad = sourceRepository.save(new Source("XYZ AD_0171", null, "Active Directory Source",
				"Active Directory", "Direct Connection", "Healthy"));

		// 15 more to reach "16 Results" -- mostly Active Directory (matching the
		// screenshot) plus a few varied connector types for realism.
		List<Source> others = List.of(
				new Source("Active Directory - East", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - West", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - Corp", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - HQ", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - Lab", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - DR", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Active Directory - EU", null, "Active Directory Source", "Active Directory",
						"Direct Connection", "Healthy"),
				new Source("Azure AD - Primary", null, "Azure Active Directory tenant", "Azure AD",
						"Direct Connection", "Healthy"),
				new Source("Azure AD - Secondary", null, "Azure Active Directory tenant", "Azure AD",
						"Direct Connection", "Healthy"),
				new Source("Workday HR", null, "Workday HR authoritative source", "Workday",
						"Direct Connection", "Healthy"),
				new Source("Workday Finance", null, "Workday Finance source", "Workday",
						"Direct Connection", "Healthy"),
				new Source("OpenLDAP Directory", null, "OpenLDAP directory source", "LDAP",
						"Direct Connection", "Healthy"),
				new Source("Oracle HR DB", null, "Oracle HR database source", "JDBC",
						"Direct Connection", "Healthy"),
				new Source("ServiceNow", null, "ServiceNow ITSM source", "ServiceNow",
						"Direct Connection", "Healthy"),
				new Source("Salesforce", null, "Salesforce CRM source", "Salesforce",
						"Direct Connection", "Healthy"));
		sourceRepository.saveAll(others);

		// 13 accounts on the AD source (mix of correlated/uncorrelated, all Enabled).
		Long sid = ad.getId();
		accountRepository.saveAll(List.of(
				new SourceAccount(sid, "Anirudh.Rajagopal", "CN=Anirudh Rajagopal,OU=Users,DC=xyz,DC=com",
						"anirudh.rajagopal", "Enabled", true),
				new SourceAccount(sid, "Brian.Clough", "CN=Brian Clough,OU=Users,DC=xyz,DC=com",
						"Brian.Clough", "Enabled", true),
				new SourceAccount(sid, "Bridgette.Jones", "CN=Bridgette Jones,OU=Users,DC=xyz,DC=com",
						"Bridgette.Jones", "Enabled", true),
				new SourceAccount(sid, "Carlos.Mendez", "CN=Carlos Mendez,OU=Users,DC=xyz,DC=com",
						"carlos.mendez", "Enabled", true),
				new SourceAccount(sid, "Dana.Whitfield", "CN=Dana Whitfield,OU=Users,DC=xyz,DC=com",
						"dana.whitfield", "Enabled", true),
				new SourceAccount(sid, "Elena.Petrova", "CN=Elena Petrova,OU=Users,DC=xyz,DC=com",
						"elena.petrova", "Enabled", true),
				new SourceAccount(sid, "Frank.Okafor", "CN=Frank Okafor,OU=Users,DC=xyz,DC=com",
						"frank.okafor", "Enabled", true),
				new SourceAccount(sid, "Grace.Liang", "CN=Grace Liang,OU=Users,DC=xyz,DC=com",
						"grace.liang", "Enabled", true),
				new SourceAccount(sid, "Hassan.Ali", "CN=Hassan Ali,OU=Users,DC=xyz,DC=com",
						"hassan.ali", "Enabled", true),
				new SourceAccount(sid, "svc-backup", "CN=svc-backup,OU=Service,DC=xyz,DC=com",
						null, "Enabled", false),
				new SourceAccount(sid, "svc-sync", "CN=svc-sync,OU=Service,DC=xyz,DC=com",
						null, "Enabled", false),
				new SourceAccount(sid, "svc-scan", "CN=svc-scan,OU=Service,DC=xyz,DC=com",
						null, "Enabled", false),
				new SourceAccount(sid, "guest.contractor", "CN=Guest Contractor,OU=Guests,DC=xyz,DC=com",
						null, "Enabled", false)));
	}
}
