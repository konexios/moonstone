@echo off & setlocal
wmic path Win32_PerfFormattedData_PerfOS_Processor get Name,PercentIdleTime /format:value
wmic cpu get LoadPercentage /FORMAT:VALUE
wmic os get lastbootuptime /FORMAT:VALUE
wmic logicaldisk get name,size,freespace
wmic OS get TotalVisibleMemorySize,FreePhysicalMemory /format:value