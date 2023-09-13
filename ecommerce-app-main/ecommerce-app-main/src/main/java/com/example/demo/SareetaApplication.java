package com.example.demo;

import com.splunk.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.beans.factory.annotation.Value;
import java.io.IOException;

@EnableJpaRepositories("com.example.demo.model.persistence.repositories")
@EntityScan("com.example.demo.model.persistence")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class SareetaApplication {

	@Value("{splunk.server.host}")
	private String SPLUNK_HOST;
	@Value("{splunk.server.port}")
	private String SPLUNK_PORT;
	@Value("{splunk.server.username}")
	private String SPLUNK_USERNAME;
	@Value("{splunk.server.password}")
	private String SPLUNK_PASSWORD;

	public static void main(String[] args) {
		SpringApplication.run(SareetaApplication.class, args);
	}



	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public TcpInput splunkService () throws IOException {
//		HttpService.setSslSecurityProtocol(SSLSecurityProtocol.TLSv1_2);
//
//		ServiceArgs loginArgs = new ServiceArgs();
//		loginArgs.setHost(SPLUNK_HOST);
//		loginArgs.setPort(SPLUNK_PORT);
//		loginArgs.setUsername(SPLUNK_USERNAME);
//		loginArgs.setPassword(SPLUNK_PASSWORD);
//
//// Create a Service instance and log in with the argument map
//		Service service = Service.connect(loginArgs);
//		IndexCollectionArgs indexcollArgs = new IndexCollectionArgs();
//		indexcollArgs.setSortKey("totalEventCount");
//		indexcollArgs.setSortDirection(IndexCollectionArgs.SortDirection.DESC);
//		IndexCollection myIndexes = service.getIndexes(indexcollArgs);
//		for (Index entity : myIndexes.values()) {
//			System.out.println("  " + entity.getName() + " (events: "
//					+ entity.getTotalEventCount() + ")");
//		}
//		for (Application app : service.getApplications().values()) {
//			System.out.println(app.getName());
//		}
//         // Get the collection of data inputs
//		InputCollection myInputs = service.getInputs();
//		// Iterate and list the collection of inputs
//		System.out.println("There are " + myInputs.size() + " data inputs:\n");
//		for (Input entity : myInputs.values()) {
//			System.out.println("  " + entity.getName() + " (" + entity.getKind() + ")");
//		}
//      // Retrieve the input
//		return (TcpInput) service.getInputs().get("3000");
//	}
}
