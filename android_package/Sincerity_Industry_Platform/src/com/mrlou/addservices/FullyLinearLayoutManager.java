package com.mrlou.addservices;

import android.support.v7.widget.RecyclerView;

import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

public class FullyLinearLayoutManager extends StaggeredGridLayoutManager {

	public FullyLinearLayoutManager(int spanCount, int orientation) {
		super(spanCount, orientation);
		// TODO Auto-generated constructor stub
	}
	 // �ߴ�����飬[0]�ǿ�[1]�Ǹ�
    private int[] measuredDimension = new int[2];

    // �����Ƚ�ͬ��/���Ǹ�item���/��
    private int[] dimension;


    @Override

    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        // ���mode+size
        final int widthMode = View.MeasureSpec.getMode(widthSpec);
        final int widthSize = View.MeasureSpec.getSize(widthSpec);
        // �ߵ�mode + size
        final int heightMode = View.MeasureSpec.getMode(heightSpec);
        final int heightSize = View.MeasureSpec.getSize(heightSpec);

        // �����ߵĳ�ʼֵ
        int width = 0;
        int height = 0;
        // item����Ŀ
        int count = getItemCount();
        // item������
        int span = getSpanCount();
        // ������������������������
        dimension = new int[span];

        for (int i = 0; i < count; i++) {
            measureScrapChild(recycler, i,
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(i, View.MeasureSpec.UNSPECIFIED), measuredDimension);

           // �������ֱ���б�����item�ĸߣ����������
            //Log.d("LISTENER", "position " + i + " height = " + measuredDimension[1]);
            if (getOrientation() == VERTICAL) {
                dimension[findMinIndex(dimension)] += measuredDimension[1];
            } else {
                dimension[findMinIndex(dimension)] += measuredDimension[0];
            }
        }
        if (getOrientation() == VERTICAL) {
            height = findMax(dimension);
        } else {
            width = findMax(dimension);
        }
        

        switch (widthMode) {
            // ���ؼ�����match_parentʱ����Ⱦ��Ǹ��ؼ��Ŀ��
            case View.MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.UNSPECIFIED:
                break;
        }
        switch (heightMode) {
            // ���ؼ�����match_parentʱ���߶Ⱦ��Ǹ��ؼ��ĸ߶�
            case View.MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case View.MeasureSpec.AT_MOST:
                break;
            case View.MeasureSpec.UNSPECIFIED:
                break;
        }
        // ���ò����ߴ�  
        setMeasuredDimension(width, height);
    }

    private void measureScrapChild(RecyclerView.Recycler recycler, int position, int widthSpec,
            int heightSpec, int[] measuredDimension) {

        // ������������item
        if (position < getItemCount()) {
            try {
                View view = recycler.getViewForPosition(position);//fix ��̬���ʱ��IndexOutOfBoundsException

                if (view != null) {
                    RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
                    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, getPaddingLeft() + getPaddingRight(), lp.width);
                    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), lp.height);
                    // ��view���в�����Ȼ�����ͨ��getMeasuredWidth()��ò����Ŀ�������
                    view.measure(childWidthSpec, childHeightSpec);
                    // ��item�Ŀ�߷���������
                    measuredDimension[0] = view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                    measuredDimension[1] = view.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                    recycler.recycleView(view);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private int findMax(int[] array) {
        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * �õ�����������СԪ�ص��±�
     *
     * @param array
     * @return
     */
    private int findMinIndex(int[] array) {
        int index = 0;
        int min = array[0];
        for (int i = 0; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
                index = i;
            }
        }
        return index;
    }
   
}
