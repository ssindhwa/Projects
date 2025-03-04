from bcc import BPF  
from ctypes import * 


# Define the file path
file_path = "adjacency_matrix.txt"

# Initialize an empty list to store the matrix
matrix = []
total_nodes = 0
# Open and read the file
with open(file_path, 'r') as file:
    for line in file:
        # Skip lines starting with '#' or empty lines
        if line.strip().startswith('#') or not line.strip():
            continue
        try:
            # Split the line into integers and append as a row to the matrix
            row = list(map(int, line.split()))
            matrix.append(row)
        except ValueError:
            print(f"Skipping invalid line: {line.strip()}")

# Print the 2D array (matrix)


bpf = BPF(src_file="logic.c")

matrix_map = bpf['matrix']

print("das ",len(matrix))
total_nodes = bpf['total_nodes']
result = bpf['result']
state = bpf['state']
start_state = 0
result[c_uint64(0)] = c_int64(start_state)
result[c_uint(1)] = c_int64(-1)
state[c_uint64(0)] = c_int64(start_state)
# n = len(matrix)
total_nodes[c_uint64(0)] = c_uint64(len(matrix))

ind = 0
for row in matrix:
    for i in row:
        # print(i)
        matrix_map[c_uint64(ind)] =c_int64(i)
        ind = ind+1

syscall = bpf.get_syscall_fnname('dummy')
bpf.attach_kprobe(event=syscall,fn_name='syscall__dummy')
# bpf.attach_tracepoint(tp="")
# bpf.attach_uprobe(name="c", sym="printf", fn_name="uprobe_printf")

# bpf.attach_uretprobe(name="c", sym="printf", fn_name="uretprobe_printf")

bpf.trace_print()
