<?php
include 'database.php';
function getDataPoints() {
    
    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Lấy dữ liệu điểm từ cơ sở dữ liệu
    $dataPoints = array();
    $query = $pdo->prepare("SELECT * FROM datapoints");
    $query->execute();
    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
        $dataPoints[] = array(
            "x" => $row['x'],
            "y" => $row['y'],
            "label" => $row['month']
        );
    }

    Database::disconnect();

    return $dataPoints;
}

function getDayData($user, $date) {

    $pdo = Database::connect();
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    $table_name = $user . '_record';

    
    $dayData = array();
    $query = $pdo->prepare("SELECT * FROM $table_name WHERE date='". $date ."'");
    $query->execute();
    while ($row = $query->fetch(PDO::FETCH_ASSOC)) {
        $dayData[] = array(
            "y" => $row['node_air'],
            "label" => $row['time']
        );
    }

    Database::disconnect();

    return $dayData;
}


?>

<!DOCTYPE HTML>
<html lang='en'>
<head>
    <script src="canvasjs.min.js"></script> 
    <script src="g1.js"></script> <!-- https://cdn.canvasjs.com/ga/canvasjs.stock.min.js -->
    <script src="c1.js"></script> <!-- https://code.jquery.com/jquery-3.6.0.min.js -->
    <script src="c2.js"></script><!-- https://code.jquery.com/ui/1.12.1/jquery-ui.js -->
    <link rel="stylesheet" href="a.css">
    <title>Graph</title>
    <style>
        body{
            margin: 0;
        }
        p{
            font-size: 25px;
        }
        .ui-datepicker.ui-widget {
            width: 86vw !important;
            /*max-width: 500px;*/
        }
        .contain{
            width: 100vw;
            margin-top: 150px;
            justify-content: center;
            align-items: center;
        }
        #chartContainer-temp{
            width: 100%;
            height: 300px;
            /*max-width: 600px;*/
            margin: 0 auto;
            margin-top: 50px;
        }
        #node_name{
            text-transform: capitalize;
        }
        .nav {
            position: fixed;
            top: 0px;
            left: 0;
            width: 100%;
            display: flex;
            justify-content: space-around;
            /*justify-content: center;
            align-items: center;*/
            height: 60px;
            margin: 0;
            z-index: 100;
        }
        .btn {
            flex: 1;
            height: fit-content;
            padding: 0px 0;
            margin: 0 0px;
            font-size: 16px;
            border: 1px solid #4CAF51;
            background-color: #4CAF50;
            color: #fff;
            text-align: center;
            text-decoration: none;
            /*border-radius: 5px;*/
            box-shadow: 2px 2px 5px rgba(0, 0, 0, 0.2);
        }
        .btn:hover{
            cursor: pointer;
            background-color: #49bf4d;
        }
        .btn-title{
            padding: 40px 0;
            font-size: 20px;
        }
        /* year section  */
        .sub-year{
            position: absolute;
            top: 62px;
            left: 0px;
            width: 100%;
            height: calc(30vh);
            background-color: #4CAF50;
            /*transform: translateY(-100%);*/
            display: grid;
            grid-template-columns: repeat(2, 1fr);
            grid-template-rows: repeat(3, 1fr);
            gap: 10px;
            transform: translateY(calc(-100% - 100px));
            transition: all ease 0.5s ;
        }
        .sub-year .item {
            /*background-color: #ddd;*/
            font-size: 20px;
            cursor: default;
            padding: 10px;
            display: flex;
            justify-content: center;
            align-items: center;
            border: 2px #49bf4d solid;
        }
        .sub-year.open{
            transform: translateY(40px);
        }

        /* month section  */
        .sub-month{
            position: absolute;
            display: flex;
            justify-content: center;
            align-items: center;
            top: 62px;
            left: 0px;
            width: 100vw;
            height: calc(20vh);
            background-color: #4CAF50;
            /*transform: translateY(-100%);*/
            transform: translateY(calc(-100% - 100px));
            transition: all ease 0.5s ;
        }
        .sub-month.open{
            transform: translateY(40px);
        }
        .sub-month button{
            color: #4CAF50;
            margin-left: 20px;
            padding: 20px 30px;
            border: 0;
            border-radius: 5%;
        }
        select {
            margin-left: 5px;
            padding: 20px 20px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
            background-color: #f2f2f2;
            color: #333;
            appearance: none;
            outline: none;
            cursor: pointer;
        }

        select:hover {
            background-color: #e6e6e6;
        }

        select:focus {
            border-color: #4285f4;
            box-shadow: 0 0 0 2px rgba(66, 133, 244, 0.3);
        }


        /* day section  */
        .sub-day{
            position: absolute;
            display: flex;
            justify-content: center;
            align-items: center;
            top: 62px;
            left: 0px;
            width: 100vw;
            height: calc(20vh);
            background-color: #4CAF50;
            /*transform: translateY(-100%);*/
            transform: translateY(calc(-100% - 100px));
            transition: all ease 0.5s ;
        }
        .sub-day.open{
            transform: translateY(40px);
        }
        
        .custom-input {
            width: 110px;
          display: inline-block;
          padding: 20px 30px;
          font-size: 16px;
          border: 1px solid #ccc;
          border-radius: 4px;
          color: #333;
          box-shadow: inset 0 2px 2px rgba(0, 0, 0, 0.1);
          transition: border-color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
        }

        .custom-input:focus {
          border-color: #5c7cfa;
          outline: none;
          box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1), 0 0 5px rgba(92, 124, 250, 0.5);
        }
        .sub-day button{
            color: #4CAF50;
            margin-left: 20px;
            padding: 20px 30px;
            border: 0;
            border-radius: 5%;
        }
        .sub-day button:hover{
            cursor: pointer;
        }
        .container-infor {
          display: flex;
          width: 100vw;
        }

        .date {
            flex: 1;
            padding: 10px;
            background-color: #f2f2f2;
            display: flex;
            justify-content: center;
            align-items: center;
        }
        /* information block  */
        .temperature {
            flex: 1;
            padding: 10px;
            background-color: #f2f2f2;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column-reverse;
        }

        .temperature-label {
            /*display: none;*/
        }
        
    </style>
</head>
<body>
<div class="contain">
    <div class="nav">
        <div class="btn">
            <div id="node_name" class="btn-title">Node</div>
        </div>
        <div class="btn" id="yearBtn">
            <div class="btn-title">Year</div>
            <div class="sub-year">
                <a class="item" >2020</a>
                <a class="item" >2021</a>
                <a class="item" >2022</a>
                <a class="item" >2023</a>
                <a class="item" >2024</a>
                <a class="item" >2025</a>
            </div>
        </div>
        <div class="btn">
            <div id="monthBtn" class="btn-title">Month</div>
            <div class="sub-month">
                <select id="monthSelect">
                    <option value="01">Jan</option>
                    <option value="02">Feb</option>
                    <option value="03">Mar</option>
                    <option value="04">Apr</option>
                    <option value="05">May</option>
                    <option value="06">Jun</option>
                    <option value="07">Jul</option>
                    <option value="08">Aug</option>
                    <option value="09">Set</option>
                    <option value="10">Oct</option>
                    <option value="11">Nov</option>
                    <option value="12">Dec</option>
                </select>
                <select id="yearSelect">
                    <option value="2020">2020</option>
                    <option value="2021">2021</option>
                    <option value="2022">2022</option>
                    <option value="2023">2023</option>
                    <option value="2024">2024</option>
                    <option value="2025">2025</option>
                </select>
                <button id="month_render">Render</button>
            </div>
        </div>
        <div class="btn">
            <div id="dayBtn" class="btn-title">Day</div>
            <div class="sub-day">
                <input type="text" id="datepicker" class="custom-input" placeholder="Choose day">
                <button id="day_render">Render</button>
            </div>
        </div>
    </div>
    <div class="container-infor">
        <div class="date">
            <p id="date"></p>
        </div>
        <div class="temperature">
            <p id="temperature"></p>
            <p class="temperature-label" id="temperature-label">Average</p>
        </div>
    </div>
    <div id="chartContainer-temp"></div>
</div>
    
    <script>
        
        window.onload = function () {
            var currentURL = new URL(window.location.href);
            var user = currentURL.searchParams.get("user");
            var node_id = currentURL.searchParams.get("node_id");

            var currentDate = new Date();
            var day = currentDate.getDate();
            var month = currentDate.getMonth() + 1; 
            var year = currentDate.getFullYear();
            var url = 'http://localhost/.iot/air/graph.php?user=' + user + '&node_id=' + node_id + '&date=' + year.toString() +'-'+month.toString()+'-'+day.toString();
            //var url = 'https://digitaldectectives.azdigi.blog/graph/graph.php?user=' + user + '&node_id=' + node_id + '&date=' + year.toString() +'-'+month.toString()+'-'+day.toString();
            $.ajax({
                url: url,
                type: 'GET',
                success: function(response) {
                    // Xử lý phản hồi từ máy chủ
                    //console.log(response);
                    var jsonObject = JSON.parse(response);
                    console.log(response);

                    var formattedDate = 'Day: ' + day + '/' + month + '/' + year;
                    document.getElementById('date').innerHTML = formattedDate;
                    var Data = {
                        type: "line",
                        dataPoints: jsonObject.graph
                    };
                    chartTemp.options.data = [Data];
                    chartTemp.render();
                    if(jsonObject.node_air != 0){
                        document.getElementById('temperature').innerHTML = parseFloat(jsonObject.node_air.toFixed(1)) + '&#8451;';
                        document.getElementById('temperature').style.color = 'black';
                    }
                    else{
                        document.getElementById('temperature').innerHTML = 'Fail Data';
                        document.getElementById('temperature').style.color = 'red';
                    }
                },
                error: function(xhr, status, error) {
                    // Xử lý lỗi (nếu có)
                    console.error(error);
                }
            });

            document.getElementById('node_name').innerHTML = node_id;

            var chartTemp = new CanvasJS.Chart("chartContainer-temp", {
                title: {
                    text: "Air humidity",
                    fontSize: 20
                },
                axisY: {
                    title: "Temperature (*C)",
                    titleFontSize: 9,
                    labelFontSize : 9
                },
                axisX: {
                    interval: 2,
                    labelAngle: -35,
                    labelFontSize: 7
                },
                data: [{
                    type: "line",
                    dataPoints: []
                }]
            });

            chartTemp.render();

            /* year js */
            document.querySelectorAll('.sub-year .item')[2].addEventListener('click', function(){
                var yearData = {
                    type: "line",
                    dataPoints: <?php echo json_encode(getDataPoints(), JSON_NUMERIC_CHECK); ?>
                };
                chartTemp.options.data = [yearData];
                chartTemp.render();
                document.getElementById('date').innerHTML = 'Year: 2022';

            })

            /* month calendar */
            var monthSelect = document.getElementById('monthSelect');
            var yearSelect = document.getElementById('yearSelect');

            var selectedMonth;
            var selectedYear;

            monthSelect.addEventListener('change', function() {
                selectedMonth = monthSelect.value;
                //console.log( selectedMonth);
            });

            yearSelect.addEventListener('change', function() {
                selectedYear = yearSelect.value;
                //console.log( selectedYear);
            });
            document.getElementById('month_render').addEventListener('click', function (){
                var url = 'http://localhost/.iot/air/graph_m.php?user=' + user + '&node_id=' + node_id + '&month=' + selectedYear.toString() +'-'+selectedMonth.toString();
                //var url = 'https://digitaldectectives.azdigi.blog/graph/graph_m.php?user=' + user + '&node_id=' + node_id + '&month=' + selectedYear.toString() +'-'+selectedMonth.toString();
                console.log(url);
                $.ajax({
                      url: url,
                      type: 'GET',
                      success: function(response) {
                        // Xử lý phản hồi từ máy chủ
                        console.log(response);
                        handleResponse(response);
                      },
                      error: function(xhr, status, error) {
                        // Xử lý lỗi (nếu có)
                        console.error(error);
                      }
                });
                function handleResponse(response) {
                        try{
                            var jsonObject = JSON.parse(response);
                            
                            var Data = {
                                type: "line",
                                dataPoints: jsonObject.graph
                            };
                            chartTemp.options.data = [Data];
                            chartTemp.render();
                            if(jsonObject.node_air != 0){
                                document.getElementById('temperature').innerHTML = parseFloat(jsonObject.node_air.toFixed(1)) + '&#8451;';
                                document.getElementById('temperature').style.color = 'black';
                            }
                            else{
                                document.getElementById('temperature').innerHTML = 'Fail Data';
                                document.getElementById('temperature').style.color = 'red';
                            }
                        }
                        catch(error){
                            document.getElementById('temperature').innerHTML = 'NaN' + '°C';
                        }
                    }
            });

            /* day calendar */
            function handleDateSelection() {
                let selectedDate;

                $("#datepicker").datepicker({
                    dateFormat: "dd/mm/yy",
                    changeMonth: true,
                    changeYear: true,
                    showButtonPanel: true,
                    onSelect: function(dateText) {
                        selectedDate = $.datepicker.parseDate("dd/mm/yy", dateText);
                        console.log(selectedDate); // Ghi dữ liệu ngày tháng năm vào console

                        // Trích xuất ngày, tháng và năm
                        let day = selectedDate.getDate();
                        let month = selectedDate.getMonth() + 1;
                        let year = selectedDate.getFullYear();
                    }
                });
            }

            $(function() {
                handleDateSelection(); // Gọi hàm xử lý khi tài liệu đã sẵn sàng

                $("#day_render").on("click", function() {
                    let selectedDate = $("#datepicker").datepicker("getDate");
                    let day = selectedDate.getDate();
                    let month = selectedDate.getMonth() + 1;
                    let year = selectedDate.getFullYear();
                    var formattedDate = 'Day: ' + day + '/' + month + '/' + year;
                    document.getElementById('date').innerHTML = formattedDate;

                    document.querySelector('.sub-day').classList.remove('open');

                    var url = 'http://localhost/.iot/air/graph.php?user=' + user + '&node_id=' + node_id + '&date=' + year.toString() +'-'+month.toString()+'-'+day.toString();
                    //var url = 'https://digitaldectectives.azdigi.blog/graph/graph.php?user=' + user + '&node_id=' + node_id + '&date=' + year.toString() +'-'+month.toString()+'-'+day.toString();
                    console.log(url);
                    $.ajax({
                      url: url,
                      type: 'GET',
                      success: function(response) {
                        // Xử lý phản hồi từ máy chủ
                        //console.log(response);
                        handleResponse(response);
                        
                      },
                      error: function(xhr, status, error) {
                        // Xử lý lỗi (nếu có)
                        console.error(error);
                      }
                    });
                    function handleResponse(response) {
                        try{
                            var jsonObject = JSON.parse(response);
                            console.log(response);
                            
                            var Data = {
                                type: "line",
                                dataPoints: jsonObject.graph
                            };
                            chartTemp.options.data = [Data];
                            chartTemp.render();
                            if(jsonObject.node_air != 0){
                                document.getElementById('temperature').innerHTML = parseFloat(jsonObject.node_air.toFixed(1)) + '&#8451;';
                                document.getElementById('temperature').style.color = 'black';
                            }
                            else{
                                document.getElementById('temperature').innerHTML = 'Fail Data';
                                document.getElementById('temperature').style.color = 'red';
                            }
                        }
                        catch(error){
                            document.getElementById('temperature').innerHTML = 'NaN' + '°C';
                        }
                    }
                });
            });
        }
        
    </script>
    <script>
        document.getElementById('dayBtn').addEventListener('click', function () {
            if(document.querySelector('.sub-day').classList.contains('open')){
                document.querySelector('.sub-day').classList.remove('open');
            }
            else{
                document.querySelector('.sub-day').classList.add('open');
            }

            if(document.querySelector('.sub-year').classList.contains('open')){
                document.querySelector('.sub-year').classList.remove('open');
            }
            if(document.querySelector('.sub-month').classList.contains('open')){
                document.querySelector('.sub-month').classList.remove('open');
            }
        });
        document.getElementById('yearBtn').addEventListener('click', function () {
            if(document.querySelector('.sub-year').classList.contains('open')){
                document.querySelector('.sub-year').classList.remove('open');
            }
            else{
                document.querySelector('.sub-year').classList.add('open');
            }

            if(document.querySelector('.sub-day').classList.contains('open')){
                document.querySelector('.sub-day').classList.remove('open');
            }
            if(document.querySelector('.sub-month').classList.contains('open')){
                document.querySelector('.sub-month').classList.remove('open');
            }
        });
        document.getElementById('monthBtn').addEventListener('click', function () {
            if(document.querySelector('.sub-month').classList.contains('open')){
                document.querySelector('.sub-month').classList.remove('open');
            }
            else{
                document.querySelector('.sub-month').classList.add('open');
            }

            if(document.querySelector('.sub-day').classList.contains('open')){
                document.querySelector('.sub-day').classList.remove('open');
            }
            if(document.querySelector('.sub-year').classList.contains('open')){
                document.querySelector('.sub-year').classList.remove('open');
            }
        });
    </script>
    <script>
    /*
    var currentDate = new Date();
    var day = currentDate.getDate();
    var month = currentDate.getMonth() + 1; 
    var year = currentDate.getFullYear();

    var formattedDate = 'Day: ' + day + '/' + month + '/' + year;
    document.getElementById('date').innerHTML = formattedDate;

    // Thay đổi nhiệt độ trung bình
    var averageTemperature = 1;
    document.getElementById('temperature').innerHTML = 1 + '&#8451;';

    // Kiểm tra nhiệt độ và hiển thị dòng chữ 'Nhiệt độ trung bình' nếu cần
    var temperatureLabel = document.getElementById('temperature-label');
    if (averageTemperature > 25) {
      temperatureLabel.style.display = 'block';
    }*/
    
  </script>
</body>
</html>
