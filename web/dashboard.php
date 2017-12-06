<?  
    $DENYTYPE = 0;
    require __DIR__."/common/base.php";
?>

<html>

	<head>

		<title>Dashboard | SafeFlight</title>

		<link rel=icon href=/img/favicon.ico>
		<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
		<link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="css/dashboard.css" />

        <script type="text/javascript">var TYPE=<?=$TYPE?>;var ID=<?=$ID?>;</script>
        <script type="text/javascript">var DISPLAY_RES=<?=isset($_GET['res'])?$_GET['res']:-1?>;</script>
        <script type="text/javascript">var EDIT_ACCOUNT=<?=isset($_GET['acc'])?$_GET['acc']:-1?>;</script>
        <script type="text/javascript">var FLIGHT_CUSTOMERS=<?=isset($_GET['flc'])?("'".$_GET['flc']."'"):"false"?>;</script>
		<script src="js/vendor/jmin.js"></script>
		<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
		<script src="js/script.js"></script>
        <script src="js/ajax.js"></script>
        <script src="js/profileedit.js"></script>
        <script src="js/dashboard.js"></script>
	</head>

	<body>
		
    	<?php $headerHighlight = "Dashboard"; include __DIR__."/common/header.php"; ?>

    	<div id="container">
    		<div id="content">

                <?php if ($TYPE == 1) { ?>

                    <h1>Customer Representative Dashboard</h1>

                    <div id="miniheader" class="miniheader">
                        Customers
                        <span class="getmailinglist">Get mailing list</span>
                        <span class="createaccountclick">Create Account</span>
                    </div>

                    <div class="modal" id="createaccount">
                        <div>
                            <div class="box">
                                <div class="spinner"><span></span></div>
                                <h2>Create Account</h2>
                                <div class="label">First Name</div>
                                <input class="input firstName" />
                                <div class="label">Last Name</div>
                                <input class="input lastName" />
                                <div class="label">Username</div>
                                <input class="input username" />
                                <div class="label">Password</div>
                                <input class="input password" type="password" />
                                <div class="message message1"></div>
                                <div class="button button2">Create</div>
                            </div>
                        </div>
                    </div>

                    <div class="modal" id="mailinglist">
                        <div>
                            <div class="box">
                                <div class="spinner"><span></span></div>
                                <h2>Mailing List</h2>
                                <textarea></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="accounts accounts1">
                        <div class="account top" account-id="-1">
                            <div class="column edit"><span>edit</span></div>
                            <div class="column username">Username</div>
                            <div class="column name">Name</div>
                            <div class="column email">Email</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                    <div class="miniheader">
                        Employees
                    </div>
                    <div class="accounts accountse accounts2">
                        <div class="account top" account-id="-1">
                            <div class="column edit"><span>edit</span></div>
                            <div class="column username">Username</div>
                            <div class="column name">Name</div>
                            <div class="column email">SSN</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                <?php } else if ($TYPE == 2) { ?>

                    <h1>Manager Dashboard</h1>

                    <div class="miniheader">
                        Employees
                        <span class="createaccountclick">Create Account</span>
                    </div>

                    <div class="accounts accountse">
                        <div class="account top" account-id="-1">
                            <div class="column edit"><span>edit</span></div>
                            <div class="column username">Username</div>
                            <div class="column name">Name</div>
                            <div class="column email">SSN</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                    <div class="modal" id="createaccount">
                        <div>
                            <div class="box">
                                <div class="spinner"><span></span></div>
                                <h2>Create Employee</h2>
                                <div class="label">First Name</div>
                                <input class="input firstName" />
                                <div class="label">Last Name</div>
                                <input class="input lastName" />
                                <div class="label">SSN</div>
                                <input class="input ssn" />
                                <div class="label">Username</div>
                                <input class="input username" />
                                <div class="label">Password</div>
                                <input class="input password" type="password" />
                                <div class="message message1"></div>
                                <div class="button button2">Create</div>
                            </div>
                        </div>
                    </div>


                    <div class="miniheader">Reservations</div>
                    <div class="fields listreservations">
                        <div class="label labeli">Flight</div>
                        <select class="flights"><option value=-1>--</option></select>
                        <div class="label labeli">OR</div>
                        <div class="label labeli">Customer</div>
                        <select class="customers"><option value=-1>--</option></select>
                    </div>
                    <div class="reservations">
                        <div class="reservation top" res-id="-1">
                            <div class="column edit"><span>view</span></div>
                            <div class="column resno">Res #</div>
                            <div class="column date">Date</div>
                            <div class="column fare">Total Fare</div>
                            <div class="column fee">Booking Fee</div>
                            <div class="column address"></div>
                        </div>
                    </div>

                    <div class="miniheader">Most Revenue</div>
                    <div class="fields mostrevenue">
                        <div class="label labeli">Show Most Generated Revenue For:</div>
                        <select class="">
                            <option value=-1>--</option>
                            <option value="getbestcustomer">Customer</option>
                            <option value="getbestcustomerrep">Customer Representatives</option>
                        </select>
                    </div>
                    <div class="mostrevenues datasets">
                        <div class="mostrevenue dataset top">
                            <div class="column revenue">Revenue</div>
                            <div class="column name">Name</div>
                            <div class="column hourlyRate">Hourly</div>
                            <div class="column start">Start</div>
                            <div class="column email">Email</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                    <div class="miniheader">Revenue Summary</div>
                    <div class="fields listrevenuesummary">
                        <div class="label labeli">Flight</div>
                        <select class="flights"><option value=-1>--</option></select>
                        <div class="label labeli">OR</div>
                        <div class="label labeli">City</div>
                        <select class="cities"><option value=-1>--</option></select>
                        <div class="label labeli">OR</div>
                        <div class="label labeli">Customer</div>
                        <select class="customers"><option value=-1>--</option></select>
                    </div>
                    <div class="revenuesummary"></div>

                    <div class="miniheader">Flight Customers</div>
                    <div class="fields listmanifest">
                        <div class="label labeli">Flight</div>
                        <select class="flights"><option value=-1>--</option></select>
                    </div>
                    <div class="manifests datasets">
                        <div class="manifest dataset top">
                            <div class="column name">Name</div>
                            <div class="column address">Address</div>
                        </div>
                    </div>

                    <div class="miniheader">Sales Report</div>
                    <div class="fields listsalesreport">
                        <div class="label labeli">Month</div>
                        <select class="date month"><option value=-1>--</option></select>
                        <div class="label labeli">Year</div>
                        <select class="date year"><option value=-1>--</option></select>
                    </div>
                    <div class="salesstats"></div>

                <?php } ?>
    			


    		</div>
    	</div>

        <? include __DIR__."/modal/resit.php"; ?>

	</body>
	
</html>




















