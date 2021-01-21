package org.deltaecore.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.deltaecore.debug.DEDebug;
import org.deltaecore.shellscript.interpretation.DEShellScriptInterpreter;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;

public class DEShell extends DEShellScriptInterpreter implements IApplication {
	@Override
	public Object start(IApplicationContext context) throws Exception {
		//Disable debug output of other components
		DEDebug.debugEnabled = false;

		final Map<?, ?> rawArgs = context.getArguments();
		final String[] args = (String[]) rawArgs.get("application.args");

		if (args != null && args.length >= 1) {
			//Use once and discard mode
			String rawShellScript = args[0];
			interpretRawShellScript(rawShellScript);
		} else {
			//Shell mode
			showCommandPrompt();
		}

		return IApplication.EXIT_OK;
	}

	@Override
	public void stop() {
	}

	private void showCommandPrompt() throws IOException {
		InputStreamReader inputStreamReader = new InputStreamReader(System.in);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		print("Welcome to the DeltaEcore shell. Type '" + COMMAND_HELP + "' for help or '" + COMMAND_HELP + " -c \"<Command>\"' for details on a specific command. Type 'exit' to leave the shell.", false);

		boolean keepRunning = true;

		try {
			do {
				System.out.print("DeltaEcore>");
				
				while (!bufferedReader.ready()) {
					Thread.sleep(10);
				}

				String line = bufferedReader.readLine();
				keepRunning = interpretRawShellScript(line);
			} while(keepRunning);
		} catch (InterruptedException e) {
		}
	}

	@Override
	protected void print(String message) {
		System.out.println(message);
	}
}
