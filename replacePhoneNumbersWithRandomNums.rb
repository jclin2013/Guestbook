lines = IO.readlines('/Users/jlin/Google Drive/Coding Stuff/javaGuestbook/seedData.sql').map do |line|
  if line.match(/1234567890/)
    randNum = ""
    10.times { randNum += rand(10).to_s }
    line = line.gsub(/1234567890/, randNum)
  end

  line
end

File.open("hello", "w") do |f|
  f.puts lines
end
