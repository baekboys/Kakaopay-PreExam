����
Rest API ��� �����ý���
����, ������ҿ�û�� �޾Ƽ� string �����ͷ� ī���� ����ϴ� ���񽺸� �����Ϸ��� �մϴ�.
�Ʒ� ��ɸ����� ���� �����ý��� API�� �����ϰ� Test �ڵ带 �ۼ��ϼ���.

API ��ɸ���

�ʼ� ����

1. ���� API
ī�������� �ݾ������� �Է¹޾Ƽ� ī���� ���ǵ� string �����ͷ� DB�� �����մϴ�.
request
required
ī���ȣ(10 ~ 16�ڸ� ����)
��ȿ�Ⱓ(4�ڸ� ����, mmyy)
cvc(3�ڸ� ����)
�Һΰ����� : 0(�Ͻú�), 1 ~ 12
�����ݾ�(100�� �̻�, 10��� ����, ����)
optional
�ΰ���ġ��

response
required
������ȣ(unique id, 20�ڸ�)
optional
�߰��� �����ָ� ���� �����͵��� �����Ӱ� �߰����ּ���.

2. ������� API
������ ���� ��ü��Ҵ� 1���� �����մϴ�.
�ΰ���ġ�� ������ �ѱ��� �ʴ� ���, ������������ �ΰ���ġ�� �ݾ����� ����մϴ�.
�Һΰ����� �����ʹ� 00(�Ͻú�)�� �����մϴ�.
request :
required
������ȣ(unique id, 20�ڸ�)
��ұݾ�
optional
�ΰ���ġ��
response
required
������ȣ(unique id, 20�ڸ�)
optional
�߰��� �����ָ� ���� �����͵��� �����Ӱ� �߰����ּ���.

3. ������ ��ȸ API
DB�� ����� �����͸� ��ȸ�ؼ� ���䰪���� ������ݴϴ�.
request : ������ȣ(unique id)
response
required
������ȣ(unique id)
ī������ : ��ȣȭ�� �����͸� ��ȣȭ�ؼ� �����͸� ������ּ���.
ī���ȣ : �� 6�ڸ��� �� 3�ڸ��� ������ �������� ����ŷó��
��ȿ�Ⱓ
cvc
����/��� ����
�ݾ�����
����/��� �ݾ�
�ΰ���ġ��
optional
�߰��� �����ָ� ���� �����͵��� �����Ӱ� �߰����ּ���.

4. API ��û ���н�
�����Ӱ� ������ ��������, �����ڵ���� �����ݴϴ�.