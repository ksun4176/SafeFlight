<header class="t3">
	<div class="header t3">

		<div class="left t3">
			<div class="logo t3"></div>
			<a href="/" class="logotext t3">SafeFlight</a>
		</div>

		<div class="right">
			<div class="links">
				<?php
					$links = array(
						array("Flights", "/flights.php"),
						array("Login", "/login.php")
					);
					$links2 = array(
						array("Account", "/account.php"),
						array("Logout", "/portals/account.php?logout")
					);

					if ($ID > -1) {
						array_pop($links);
						$links = array_merge($links, $links2);
					}

					for($i=0;$i<count($links);$i++) {
						$active = "";
						if ($headerHighlight == $links[$i][0]) $active = "active";
						?>
						<a href="<?=$links[$i][1]?>" class="link t2 <?=$active?>">
							<div class="text t2">
								<?=$links[$i][0]?>
							</div>
						</a>
					<? }
				?>
			</div>
		</div>

	</div>
</header>